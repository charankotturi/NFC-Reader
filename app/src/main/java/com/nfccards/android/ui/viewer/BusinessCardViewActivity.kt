package com.nfccards.android.ui.viewer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nfccards.android.R
import com.nfccards.android.databinding.ActivityBusinessCardViewBinding
import com.nfccards.android.resources.Utils

class BusinessCardViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessCardViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_business_card_view)

        var data = intent.getStringExtra("data")
        if (data == null) {
            data = ""
        }
        val model = Utils.getBusinessModel(data)

        binding.txtName.text = model.name
        binding.txtBusinessName.text = model.business
        binding.txtWebsite.text = model.webSite
        binding.txtPhoneNum.text = model.phoneNum

        // To open website from the business card
        binding.llWebsite.setOnClickListener {
            try {
                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.webSite))
                startActivity(myIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this, "something went wrong!", Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
            }
        }

        // To open phone app from the business card
        binding.llPhoneNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${model.phoneNum}")
            startActivity(intent)
        }
    }
}