package com.tabzero.jetpack.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.tabzero.jetpack.R
import com.tabzero.jetpack.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding
    private val pageNumber = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

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

    fun setPageNumber(value: Int){
        pageNumber.value = value
    }
}