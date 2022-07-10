package com.nfccards.android

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.nfccards.android.databinding.ActivityMainBinding
import com.nfccards.android.model.User
import com.nfccards.android.ui.history.HistoryActivity
import com.nfccards.android.ui.onboarding.SignInActivity
import com.nfccards.android.ui.phone_cards.PhoneActivity
import com.nfccards.android.ui.scanner.ScanActivity
import com.nfccards.android.ui.writer.MyCardsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val auth = Firebase.auth
        val sharedPreferences: SharedPreferences = getSharedPreferences("MY_DB", MODE_PRIVATE)!!
        val currentUserJson = sharedPreferences.getString("current_user", "")
        if (currentUserJson != ""){
            user = Gson().fromJson(currentUserJson, User::class.java)
        }

        if (auth.currentUser != null){
            if (user == null){
                return
            }
            binding.txtUserName.text = "⦿  ${user?.username}"
        }

        binding.imgLogOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding.btnMyPhone.setOnClickListener {
            startActivity(Intent(this, PhoneActivity::class.java))
        }

        binding.btnRead.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            intent.putExtra("data", "")
            startActivity(intent)
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            intent.putExtra("data", "")
            intent.putExtra("edit", true)
            startActivity(intent)
        }

        binding.btnWrite.setOnClickListener {
            val intent = Intent(this, MyCardsActivity::class.java)
            startActivity(intent)
        }

        binding.btnHistory.setOnClickListener {
            if (auth.currentUser == null) {
                Toast.makeText(this@MainActivity, "Login to get history!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}