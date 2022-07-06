package com.tabzero.jetpack.ui.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.tabzero.jetpack.R
import com.tabzero.jetpack.databinding.FragmentOtpBinding
import java.lang.ClassCastException

class Otp : Fragment() {

    private lateinit var binding: FragmentOtpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false )

        binding.btnSend.setOnClickListener {
            findNavController().navigate(R.id.action_otp_to_userName)
        }



        return binding.root
    }

    override fun onStart() {
        try {
            (activity as SignInActivity).setPageNumber(1)
        }catch (e: ClassCastException){e.printStackTrace()}

        super.onStart()
    }
}