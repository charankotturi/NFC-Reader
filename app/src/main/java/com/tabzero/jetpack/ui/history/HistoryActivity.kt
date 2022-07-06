package com.tabzero.jetpack.ui.history

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.tabzero.jetpack.R
import com.tabzero.jetpack.databinding.ActivityHistoryBinding
import com.tabzero.jetpack.model.BusinessModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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

        val historyCardsList = Json.decodeFromString<List<BusinessModel>>(string = scannedCards)
        if (historyCardsList.isEmpty()){
            return
        }
        binding.emptyListView.visibility = View.GONE

        val adapter = HistoryCard()
        adapter.diffUtils.submitList(historyCardsList)

        binding.rcCardsList.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@HistoryActivity)
        }
    }
}