package com.nfccards.android

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.nfccards.android.databinding.ActivityMainBinding
import com.nfccards.android.model.User
import com.nfccards.android.resources.Resource
import com.nfccards.android.ui.history.HistoryActivity
import com.nfccards.android.ui.onboarding.SignInActivity
import com.nfccards.android.ui.phone_cards.PhoneActivity
import com.nfccards.android.ui.scanner.ScanActivity
import com.nfccards.android.ui.writer.MyCardsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var user: User? = null
    private var liveUser = MutableLiveData<Resource<User>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        liveUser.value = Resource.Loading()

        val auth = Firebase.auth
        val sharedPreferences: SharedPreferences = getSharedPreferences("MY_DB", MODE_PRIVATE)!!
        val currentUserJson = sharedPreferences.getString("current_user", "")
        if (currentUserJson != ""){
            user = Gson().fromJson(currentUserJson, User::class.java)
            user?.let {
                liveUser.value = Resource.Success(data = it, message = "")
            }
        }

        if (auth.currentUser == null){
            binding.txtUserName.visibility = View.GONE
        }

        val mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (!mNfcAdapter.isEnabled) {
            val alertbox: AlertDialog.Builder = AlertDialog.Builder(this)
            alertbox.setTitle("Info")
            alertbox.setMessage("NFC is not enabled right now")
            alertbox.setPositiveButton("Turn On",
                DialogInterface.OnClickListener { dialog, which ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                        startActivity(intent)
                    } else {
                        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(intent)
                    }
                })
            alertbox.setNegativeButton("Close",
                DialogInterface.OnClickListener { dialog, which -> })
            alertbox.show()
        }

        liveUser.observe(this){
            when (it){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                }
            }
        }

        if (auth.currentUser != null){
            if (user == null){
                return
            }
            binding.txtUserName.text = "⦿  ${user?.username}"
        }

        binding.imgLogOut.setOnClickListener {
            auth.signOut()
            val edit = sharedPreferences.edit()
            edit.clear()
            edit.commit()

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

    override fun onStart() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MY_DB", MODE_PRIVATE)!!
        val currentUserJson = sharedPreferences.getString("current_user", "")
        if (currentUserJson != ""){
            user = Gson().fromJson(currentUserJson, User::class.java)
            user?.let {
                liveUser.value = Resource.Success(data = it)
            }
        }

        super.onStart()
    }
}