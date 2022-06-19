package com.tabzero.jetpack

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tabzero.jetpack.databinding.ActivityBusinessCardViewBinding
import com.tabzero.jetpack.resources.Utils


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

        binding.textView3.text = model.name
        binding.textView4.text = model.business
        binding.textview5.text = model.webSite
        binding.textview6.text = model.phoneNum

        binding.linearLayout2.setOnClickListener {
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

        binding.llPhoneNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${model.phoneNum}")
            startActivity(intent)
        }
    }
}