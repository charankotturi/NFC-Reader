package com.nfccards.android.ui.writer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.nfccards.android.R
import com.nfccards.android.databinding.ActivityMyCardsBinding
import com.nfccards.android.resources.Utils
import com.nfccards.android.ui.scanner.ScanActivity

class MyCardsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyCardsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cards)
        val data = intent.getStringExtra("data")

        if (data != null) {
            val model = Utils.getBusinessModel(data)

            binding.etBname.setText(model.business)
            binding.etName.setText(model.name)
            binding.etWebsite.setText(model.webSite)
            binding.etPhoneNumber.setText(model.phoneNum)
        }

        binding.btnEdit.setOnClickListener {
            if (binding.etBname.text?.isEmpty() == true){
                toast("enter valid business name!")
                return@setOnClickListener
            }
            if (binding.etName.text?.isEmpty() == true){
                toast("enter valid name!")
                return@setOnClickListener
            }
            if (binding.etPhoneNumber.text?.isEmpty() == true){
                toast("enter a phone number!")
                return@setOnClickListener
            }
            if (binding.etWebsite.text?.isEmpty() == true){
                toast("enter your website!")
                return@setOnClickListener
            }

            val intent = Intent(this, ScanActivity::class.java)
            intent.putExtra("isReader", false)
            intent.putExtra("data", "${binding.etName.text},${binding.etBname.text},${binding.etPhoneNumber.text},${binding.etWebsite.text}")
            startActivity(intent) }

        binding.imgBack.setOnClickListener {
            finish() }
    }

    private fun toast(message: String){
        Toast.makeText(this@MyCardsActivity, message, Toast.LENGTH_SHORT).show()
    }
}