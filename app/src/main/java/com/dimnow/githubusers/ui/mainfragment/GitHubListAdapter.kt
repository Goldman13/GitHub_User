package com.dimnow.githubusers.ui.mainfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dimnow.githubusers.api.User
import com.dimnow.githubusers.api.UserDetail
import com.dimnow.githubusers.databinding.UserItemBinding

class GitHubListAdapter(val click:(UserDetail)->Unit)
    : ListAdapter<UserDetail, GitHubListAdapter.GitHubViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return GitHubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GitHubViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(getItem(position).avatar_url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.binding.avatar)
        val currentItem = getItem(position)
        holder.binding.nickname.text = currentItem.name?:""
        holder.itemView.setOnClickListener {
            click(currentItem)
        }
    }

    class GitHubViewHolder(val binding: UserItemBinding):RecyclerView.ViewHolder(binding.root)

    class DiffCallback:DiffUtil.ItemCallback<UserDetail>(){
        override fun areItemsTheSame(oldItem: UserDetail, newItem: UserDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserDetail, newItem: UserDetail): Boolean {
            return oldItem.id == newItem.id
        }
    }
}