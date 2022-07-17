package com.nfccards.android.ui.onboarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nfccards.android.MainActivity
import com.nfccards.android.R
import com.nfccards.android.databinding.ActivitySignInBinding
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: SignInViewModel
    private val pageNumber = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        viewModel = ViewModelProviders.of(this)[SignInViewModel::class.java]

        binding.txtSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        pageNumber.observe(this){ value ->
            when (value) {
                1 -> {
                    binding.cvFragTwo.setCardBackgroundColor(Color.Gray.toArgb())
                    binding.cvFragOne.setCardBackgroundColor(Color.White.toArgb())
                    binding.cvFragThree.setCardBackgroundColor(Color.White.toArgb())
                }
                2 -> {
                    binding.cvFragTwo.setCardBackgroundColor(Color.White.toArgb())
                    binding.cvFragOne.setCardBackgroundColor(Color.White.toArgb())
                    binding.cvFragThree.setCardBackgroundColor(Color.Gray.toArgb())
                }
                else -> {
                    binding.cvFragTwo.setCardBackgroundColor(Color.White.toArgb())
                    binding.cvFragOne.setCardBackgroundColor(Color.Gray.toArgb())
                    binding.cvFragThree.setCardBackgroundColor(Color.White.toArgb())
                }
            }
        }
    }

    fun getSignInViewModel(): SignInViewModel = viewModel

    fun setPageNumber(value: Int){
        pageNumber.value = value
    }
}