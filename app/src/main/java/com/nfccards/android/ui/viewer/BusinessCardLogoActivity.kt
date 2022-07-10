package com.nfccards.android.ui.viewer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.nfccards.android.R
import com.nfccards.android.databinding.ActivityBusinessCardLogoBinding
import com.nfccards.android.databinding.ActivityBusinessCardViewBinding
import com.nfccards.android.databinding.CardBusinessLogoBinding
import com.nfccards.android.model.User
import com.nfccards.android.resources.Utils

class BusinessCardLogoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessCardLogoBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_business_card_logo)

        var data = intent.getStringExtra("data")
        if (data == null) {
            data = ""
        }
        val model = Utils.getBusinessLogoModel(data)

        binding.cardLogo.txtBusinessName.setText(model.business)
        binding.cardLogo.txtName.setText(model.name)
        binding.cardLogo.txtPosition.setText(model.position)
        binding.etPhoneNumber.setText(model.phoneNum)
        binding.etWebLink.setText(model.webSite)
        binding.etLogoUrl.setText(model.logoUrl)

        if (intent.getBooleanExtra("isReader", true)){
            binding.btnSubmit.visibility = View.GONE
            binding.etLogoUrl.visibility = View.GONE
            binding.etWebLink.visibility = View.GONE
            binding.etPhoneNumber.visibility = View.GONE

            binding.cardLogo.txtBusinessName.isEnabled = false
            binding.cardLogo.txtName.isEnabled = false
            binding.cardLogo.txtPosition.isEnabled = false

            // To open website from the business card
            binding.cardLogo.llWebsite.setOnClickListener {
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
            binding.cardLogo.llPhoneNumber.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${model.phoneNum}")
                startActivity(intent)
            }
        }

        binding.btnSubmit.setOnClickListener {
            val name = binding.cardLogo.txtName.text.toString()
            val businessName = binding.cardLogo.txtBusinessName.text.toString()
            val position = binding.cardLogo.txtPosition.text.toString()
            val webLink = binding.etWebLink.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val logoUrl = binding.etLogoUrl.text.toString()

            val sharedPreferences: SharedPreferences = getSharedPreferences("MY_DB", MODE_PRIVATE)!!
            val currentUserJson = sharedPreferences.getString("current_user", "")
            if (currentUserJson != ""){
                user = Gson().fromJson(currentUserJson, User::class.java)
            }else{
                Toast.makeText(this, "Login First!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = Firebase.firestore

            val user = hashMapOf(
                "name" to name,
                "business" to businessName,
                "phoneNum" to phoneNumber,
                "webSite" to webLink,
                "logoUrl" to logoUrl,
                "position" to position,
                "userPhoneNumber" to user?.phoneNum
            )

            if (model.id != null){
                user["id"] = model.id

                db.collection("post")
                    .document(model.id)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Successfully added the model", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    }
                return@setOnClickListener
            }
            db.collection("post")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    user["id"] = documentReference.id
                    db.collection("post")
                        .document(documentReference.id)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Successfully added the model", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Something went wrong.\ntry again later!", Toast.LENGTH_SHORT).show()

                }
        }

    }
}