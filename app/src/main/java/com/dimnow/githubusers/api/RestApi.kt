package com.dimnow.githubusers.api

import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApi {

    companion object{
        const val BASE_URL = "https://api.github.com"
    }

    @GET("/users")
    fun getListUsers(
        @Query("since") since:Long,
        @Query("per_page") per_page:Int
    ):Single<List<User>>

    @GET("/users/{user}")
    fun getUserDetail(@Path("user") user:String):Single<UserDetail>

}

