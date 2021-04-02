package com.dimnow.githubusers.ui.detailfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dimnow.githubusers.databinding.DetailFragmentBinding
import dagger.android.support.DaggerFragment

class DetailFragment:DaggerFragment() {

    companion object{
        const val LINK = "link"
        const val AVATAR = "avatar_link"
        const val NICKNAME = "nickname"
        const val LOCATION = "location"
    }

    lateinit var binding:DetailFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let{ bundle ->
            binding.link.text = bundle.getString(LINK,"")
            binding.location.text = bundle.getString(LOCATION,"")
            Glide.with(this)
                .load(bundle.getString(AVATAR,""))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.avatarDetail)
            binding.nicknameDetail.text = bundle.getString(NICKNAME,"")
        }
    }
}