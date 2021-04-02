package com.dimnow.githubusers.ui

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.dimnow.githubusers.databinding.TokenDialogBinding
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class TokenDialogFragment:DaggerDialogFragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    companion object{
        const val TOKEN = "githubtoken"
    }

    lateinit var binding:TokenDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alert = activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            binding = TokenDialogBinding.inflate(inflater)
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        binding.buttonTokenOk.setOnClickListener {
            if(!binding.textToken.text.isNullOrEmpty()){
                activity?.let{
                    sharedPreferences
                            .edit()
                            .putString(TOKEN,binding.textToken.text.toString())
                            .apply()
                }
            }
            alert.cancel()
        }
        return alert
    }
}
