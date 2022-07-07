package com.nfccards.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nfccards.android.databinding.ActivityMainBinding
import com.nfccards.android.ui.history.HistoryActivity
import com.nfccards.android.ui.onboarding.SignInActivity
import com.nfccards.android.ui.scanner.ScanActivity
import com.nfccards.android.ui.writer.MyCardsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val auth = Firebase.auth

        // Get User Name

        if (auth.currentUser != null){
            binding.txtUserName.text = ""
        }

        binding.imgLogOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
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