package com.tabzero.jetpack.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.tabzero.jetpack.MainActivity
import com.tabzero.jetpack.R
import com.tabzero.jetpack.databinding.FragmentUserNameBinding
import java.lang.ClassCastException

class UserName : Fragment() {

    private lateinit var binding: FragmentUserNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_name, container, false)

        binding.btnSend.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }

        return binding.root
    }

    override fun onStart() {
        try {
            (activity as SignInActivity).setPageNumber(2)
        }catch (e: ClassCastException){e.printStackTrace()}

        super.onStart()
    }
}