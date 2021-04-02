package com.dimnow.githubusers.ui.mainfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dimnow.githubusers.R
import com.dimnow.githubusers.databinding.MainFragmentBinding
import com.dimnow.githubusers.ui.MainActivity
import com.dimnow.githubusers.ui.detailfragment.DetailFragment
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainFragment:DaggerFragment() {

    @Inject
    lateinit var viewModelFactory:ViewModelProvider.Factory
    private val viewModel:MainViewModel by viewModels{ viewModelFactory }
    lateinit var binding:MainFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(viewModel.allList.isEmpty()) showSplashScreen()

        val gitHubListAdapter = GitHubListAdapter(){ userDetail ->
            val bundle = Bundle().apply{
                putString(DetailFragment.NICKNAME, userDetail.login)
                putString(DetailFragment.AVATAR, userDetail.avatar_url)
                putString(DetailFragment.LOCATION, userDetail.location)
                putString(DetailFragment.LINK, userDetail.html_url)
            }
            val fragment = DetailFragment().apply {
                arguments = bundle
            }
            (activity as MainActivity).getFragment(fragment, true)
        }

        binding.recyclerGithublist.apply {
            adapter = gitHubListAdapter
            val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider_user_list,
                    null)!!)
            addItemDecoration(itemDecoration)
        }

        viewModel.newAddSize.observe(viewLifecycleOwner,{size ->
            if(gitHubListAdapter.itemCount>0)
                gitHubListAdapter.notifyItemInserted(viewModel.allList.size - size)
            else
                gitHubListAdapter.submitList(viewModel.allList)

            if(size < viewModel.pageLimit) viewModel.isLastPage = true
            viewModel.isLoadingNextPage = false
        })

        viewModel.error.observe(viewLifecycleOwner,{
            Snackbar.make(view,it,Snackbar.LENGTH_LONG).show()
            viewModel.isLoadingNextPage = false
        })

        binding.recyclerGithublist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!viewModel.isLoadingNextPage && !viewModel.isLastPage)
                    with(recyclerView.layoutManager as LinearLayoutManager) {
                        if(itemCount - findFirstVisibleItemPosition()<=childCount){
                            viewModel.isLoadingNextPage = true
                            viewModel.loadGitHubUserList()
                        }
                    }
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun showSplashScreen(){
        binding.splashScreen.visibility = View.VISIBLE
        Observable.timer(3L,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            binding.splashScreen.visibility = View.GONE
                        }
                )
    }
}