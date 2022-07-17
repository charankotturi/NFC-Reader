package com.nfccards.android.ui.viewer

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.nfccards.android.R
import com.nfccards.android.databinding.ActivityBusinessCardViewBinding
import com.nfccards.android.model.BusinessModel
import com.nfccards.android.resources.Utils
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Exception

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

        var list = ArrayList<BusinessModel>()
        val sharedPref = this.getSharedPreferences("DB_NAME" , Context.MODE_PRIVATE) ?: return
        val scannedCards: String = sharedPref.getString("Scanned_Cards", "")!!
       try {
           val historyCardsList = Json.decodeFromString<List<BusinessModel>>(string = scannedCards)
           list = historyCardsList as ArrayList<BusinessModel>
           var exits = false
           historyCardsList.forEach {
               if (it.id == model.id){
                   exits = true
               }
           }

           if (!exits){
               list.add(model)
               val edit = sharedPref.edit()
               edit.putString("Scanned_Cards", Json.encodeToString<List<BusinessModel>>(list))
               edit.commit()
           }
        } catch (E: Exception) {
           list.add(model)
           val edit = sharedPref.edit()
           edit.putString("Scanned_Cards", Gson().toJson(list))
           edit.commit()
        }

        binding.txtName.text = model.name
        binding.txtBusinessName.text = model.business
        binding.txtWebsite.text = model.webSite
        binding.txtPhoneNum.text = model.phoneNum

        if (model.webSite.isEmpty()){
            binding.llWebsite.visibility = View.GONE
        }

        if (model.phoneNum.isEmpty()){
            binding.llWebsite.visibility = View.GONE
        }

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