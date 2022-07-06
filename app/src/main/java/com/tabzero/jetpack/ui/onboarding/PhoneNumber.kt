package com.tabzero.jetpack.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.tabzero.jetpack.MainActivity
import com.tabzero.jetpack.R
import com.tabzero.jetpack.databinding.FragmentPhoneNumberBinding
import java.lang.ClassCastException

class PhoneNumber : Fragment() {

    private lateinit var binding: FragmentPhoneNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_number, container, false)

        binding.btnSend.setOnClickListener {
            findNavController().navigate(R.id.action_phoneNumber_to_otp)
        }

        return binding.root
    }

    override fun onStart() {
        try {
            (activity as SignInActivity).setPageNumber(0)
        }catch (e: ClassCastException){e.printStackTrace()}

        super.onStart()
    }

}