package com.nfccards.android.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nfccards.android.MainActivity
import com.nfccards.android.R
import com.nfccards.android.databinding.FragmentPhoneNumberBinding
import java.lang.ClassCastException
import java.util.concurrent.TimeUnit

class PhoneNumber : Fragment() {

    private lateinit var binding: FragmentPhoneNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_number, container, false)
        val auth = Firebase.auth

        auth.currentUser?.let {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        binding.btnSend.setOnClickListener {
            binding.etPhoneNumber.text.toString().length.let {
                if (it < 10){
                    toast("Enter a valid number!")
                    return@setOnClickListener
                }
            }

            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    (activity as SignInActivity).getSignInViewModel().credentials = credential
                    findNavController().navigate(R.id.action_phoneNumber_to_userName)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.

                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Invalid request
                            toast(message = "Invalid request!")
                        }
                        is FirebaseTooManyRequestsException -> {
                            // The SMS quota for the project has been exceeded
                            toast(message = "Sms quota exceeded!")
                        }
                        else -> {
                            toast(message = "some error has occurred!")
                        }
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    findNavController().navigate(R.id.action_phoneNumber_to_otp)

                    // Save verification ID and resending token so we can use them later
                    (activity as SignInActivity).getSignInViewModel().setPhoneNumber(binding.etPhoneNumber.text.toString() as Int)
                    (activity as SignInActivity).getSignInViewModel().verificationId = verificationId
                    (activity as SignInActivity).getSignInViewModel().resendToken = token
//                    storedVerificationId = verificationId
//                    resendToken = token
                }
            }

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91" + binding.etPhoneNumber.text.toString())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        return binding.root
    }

    override fun onStart() {
        try {
            (activity as SignInActivity).setPageNumber(0)
        }catch (e: ClassCastException){e.printStackTrace()}

        super.onStart()
    }

    fun toast(message: String){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}