package com.dimnow.githubusers.di

import android.content.Context
import android.content.SharedPreferences
import com.dimnow.githubusers.GitHubApplication
import com.dimnow.githubusers.api.RestApi
import com.dimnow.githubusers.ui.TokenDialogFragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun getAppContext(app: GitHubApplication) = app.applicationContext

    @Singleton
    @Provides
    fun sharedPref(appContext:Context):SharedPreferences{
        return appContext.getSharedPreferences("commonfile", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun getMoshi():Moshi{
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun getHttpClient(sharedPreferences: SharedPreferences):OkHttpClient{
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder().addInterceptor(logging)
        .addNetworkInterceptor(object :Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val builder =  chain.request()
                .newBuilder()
                .header("Accept","application/vnd.github.v3+json")
                with(sharedPreferences){
                    if(contains(TokenDialogFragment.TOKEN))
                        builder.header("Authorization", "token "+getString(TokenDialogFragment.TOKEN,""))
                }
                return chain.proceed(builder.build())
            }
        }).build()
    }

    @Singleton
    @Provides
    fun provideService(moshi:Moshi, httpClient:OkHttpClient):RestApi{
        val retrofit = Retrofit.Builder()
                .client(httpClient)
                .baseUrl(RestApi.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(RestApi::class.java)
    }
}