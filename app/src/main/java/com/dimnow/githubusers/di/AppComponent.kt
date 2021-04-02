package com.dimnow.githubusers.di

import com.dimnow.githubusers.GitHubApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules=[
        AndroidInjectionModule::class,
        AppModule::class,
        FragmentModule::class,
        ViewModelModule::class]
)
interface AppComponent:AndroidInjector<GitHubApplication>{
    @Component.Factory
    interface Factory:AndroidInjector.Factory<GitHubApplication>{}
}