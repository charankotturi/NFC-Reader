package com.tabzero.jetpack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.tabzero.jetpack.databinding.ActivityMainBinding
import com.tabzero.jetpack.ui.history.HistoryActivity
import com.tabzero.jetpack.ui.scanner.ScanActivity
import com.tabzero.jetpack.ui.writer.MyCardsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}