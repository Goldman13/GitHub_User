package com.dimnow.githubusers.di

import androidx.fragment.app.Fragment
import com.dimnow.githubusers.ui.TokenDialogFragment
import com.dimnow.githubusers.ui.detailfragment.DetailFragment
import com.dimnow.githubusers.ui.mainfragment.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {
    @ContributesAndroidInjector
    fun bindMainFragment():MainFragment

    @ContributesAndroidInjector
    fun bindDetailFragment(): DetailFragment

    @ContributesAndroidInjector
    fun bindTokenDialogFragment(): TokenDialogFragment
}