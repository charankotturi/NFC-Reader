package com.nfccards.android.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nfccards.android.R
import com.nfccards.android.databinding.FragmentOtpBinding
import com.nfccards.android.resources.Resource
import java.lang.ClassCastException
import java.lang.Exception

class Otp : Fragment() {

    private lateinit var binding: FragmentOtpBinding
    private lateinit var auth: FirebaseAuth
    private var loading = MutableLiveData<Resource<Int>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false )
        auth = Firebase.auth
        (activity as SignInActivity).getSignInViewModel().credentials?.let {
            signInWithPhoneAuthCredential(
                it
            )
        }

        loading.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        binding.btnSend.setOnClickListener {
            loading.value = Resource.Loading()

            try {
                val credential = PhoneAuthProvider.getCredential(
                    (activity as SignInActivity).getSignInViewModel().verificationId!!
                    , binding.otpView.otp!!
                )

                signInWithPhoneAuthCredential(credential)
            }catch (e: Exception){
                e.printStackTrace()
                loading.value = Resource.Error(message = "")
                toast("something went wrong, try again!")
            }
        }

        binding.txtPhoneNumber.text = "+91 ${(activity as SignInActivity).getSignInViewModel().phoneNumber}"

        binding.imgBack.setOnClickListener {
            findNavController().navigate(R.id.action_otp_to_phoneNumber)
        }

        return binding.root
    }

    override fun onStart() {
        try {
            (activity as SignInActivity).setPageNumber(1)
        }catch (e: ClassCastException){e.printStackTrace()}

        super.onStart()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user

                    findNavController().navigate(R.id.action_otp_to_userName)
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        loading.value = Resource.Error(message = "")
                        toast("verification code invalid!")
                    }
                }
            }
    }

    fun toast(message: String){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}