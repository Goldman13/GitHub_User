package com.dimnow.githubusers

import com.dimnow.githubusers.di.AppComponent
import com.dimnow.githubusers.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class GitHubApplication():DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}