package com.dimnow.githubusers.ui.mainfragment

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimnow.githubusers.api.RestApi
import com.dimnow.githubusers.api.User
import com.dimnow.githubusers.api.UserDetail
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val gitHubService:RestApi
):ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var index:Long = 0
    val pageLimit:Int = 10

    var isLoadingNextPage = false
    var isLastPage = false

    private val _newAddSize = MutableLiveData<Int>()
    val newAddSize: LiveData<Int> = _newAddSize

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    var allList:MutableList<UserDetail> = mutableListOf()

    init{
        loadGitHubUserList()
    }

    @SuppressLint("CheckResult")
    fun loadGitHubUserList(){
        gitHubService.getListUsers(index,pageLimit)
            .subscribeOn(Schedulers.io())
            .flatMapObservable{it.toObservable()}
            .flatMapSingle{gitHubService.getUserDetail(it.login)}
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    index = it.last().id
                    allList.addAll(it)
                    _newAddSize.value = it.size
                },
                onError = {
                    _error.value = it.message?:"Error!!!"
                }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}