package com.nfccards.android.ui.history

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nfccards.android.R
import com.nfccards.android.databinding.ActivityHistoryBinding
import com.nfccards.android.model.BusinessModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Exception

class HistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)

        binding.imgBack.setOnClickListener { finish() }

        val sharedPref = this.getSharedPreferences("DB_NAME" ,Context.MODE_PRIVATE) ?: return
        val scannedCards: String = sharedPref.getString("Scanned_Cards", "")!!

        if (scannedCards == ""){
            binding.emptyListView.visibility = View.VISIBLE
            return
        }

        try {
            val token = object : TypeToken<List<BusinessModel>>() {}.type
            val historyCardsList = Gson().fromJson<List<BusinessModel>>(scannedCards, token)
            if (historyCardsList.isEmpty()) {
                throw Exception()
            }
            binding.emptyListView.visibility = View.GONE
            binding.rcCardsList.visibility = View.VISIBLE

            val adapter = HistoryCard()
            adapter.diffUtils.submitList(historyCardsList)
            binding.rcCardsList.apply {
                this.adapter = adapter
                this.layoutManager = LinearLayoutManager(this@HistoryActivity, LinearLayoutManager.VERTICAL, false)
            }

            adapter.notifyDataSetChanged()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}