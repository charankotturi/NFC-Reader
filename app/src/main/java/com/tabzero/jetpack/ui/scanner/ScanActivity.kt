package com.tabzero.jetpack.ui.scanner

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.*
import android.nfc.NdefRecord.createMime
import android.nfc.tech.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.tabzero.jetpack.ui.viewer.BusinessCardViewActivity
import com.tabzero.jetpack.ui.writer.MyCardsActivity
import com.tabzero.jetpack.R
import com.tabzero.jetpack.databinding.ActivityScanBinding
import java.nio.charset.Charset
import java.util.*


const val TAG = "Asdfsdfsdf"

class ScanActivity : AppCompatActivity() {
    
    private var nfcAdapter: NfcAdapter? = null
    private var intentFiltersArray: Array<IntentFilter>? = null
    private var techListsArray: Array<Array<String>>? = null
    private var pendingIntent: PendingIntent? = null

    private lateinit var binding: ActivityScanBinding
    private lateinit var viewModel: ScannerViewModel

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var data = intent.getStringExtra("data")
        if (data == null) {
            data = ""
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan)
        val factory = ScannerViewModelFactory(isReader = intent.getBooleanExtra("isReader", true)
            , data = data
            , edit = intent.getBooleanExtra("edit", false)
        )
        viewModel = ViewModelProviders.of(this, factory).get(ScannerViewModel::class.java)

        if (viewModel.viewEdit){
            binding.textView.text = "Edit"
        }else if (viewModel.isReadingCard){
            binding.textView.text = "Read"
        }
        
        val nfcManager = getSystemService(Context.NFC_SERVICE) as NfcManager?
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcManager != null) {
            println("NFC Manager ready ...")
        }

        if (nfcAdapter != null) {
            println("NFC Adapter ready ...")
        }

        pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, this.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        intentFiltersArray = arrayOf(IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))
        techListsArray = arrayOf(
            arrayOf(NfcA::class.java.name), arrayOf(
                NfcB::class.java.name
            ), arrayOf(IsoDep::class.java.name)
        )
    }

    override fun onResume() {
        super.onResume()

        if (nfcAdapter != null) {
            nfcAdapter!!.enableForegroundDispatch(
                this,
                pendingIntent,
                null,
                null
            )
        }

        if (viewModel.isReadingCard){
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                processIntent(intent)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (nfcAdapter != null) {
            try {
                nfcAdapter!!.disableForegroundDispatch(this)
            } catch (ex: IllegalStateException) {
                Log.e("ATHTAG", "Error disabling NFC foreground dispatch", ex)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val tag: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        setIntent(intent)

//        tag?.let { TagData.detectTagData(it) }

        if (tag != null) {
            tag.techList.forEach {
                Log.i(TAG, "onNewIntent: Yay!")
            }

            val nfc = NfcA.get(tag)

            try {
                if (!viewModel.isReadingCard){
                    writeTag(tag, NdefMessage(
                        arrayOf(
                            createMime("text/plain", viewModel.viewData.toByteArray())
                        )
                    ))
                }
            }catch (e: Exception){
                Toast.makeText(this@ScanActivity, "Something went wrong try again!", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        } else {
            println("No new tag found !!!")
        }
    }

    private fun processIntent(intent: Intent) {
        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMsgs ->
            (rawMsgs[0] as NdefMessage).apply {
                Log.i(TAG, "processIntent: ${String(records[0].payload)}")
                finish()

                if (viewModel.viewEdit){
                    val newIntent = Intent(this@ScanActivity, MyCardsActivity::class.java)
                    newIntent.putExtra("data", String(records[0].payload))
                    startActivity(newIntent)
                }else{
                    val newIntent = Intent(this@ScanActivity, BusinessCardViewActivity::class.java)
                    newIntent.putExtra("data", String(records[0].payload))
                    startActivity(newIntent)
                }
            }
        }
    }

    private fun writeTag(tag: Tag?, message: NdefMessage?) {
        if (tag != null) {
            try {
                val ndefTag = Ndef.get(tag)
                if (ndefTag == null) {
                    // Let's try to format the Tag in NDEF
                    val nForm = NdefFormatable.get(tag)
                    if (nForm != null) {
                        nForm.connect()
                        nForm.format(message)
                        Log.i(TAG, "writeTag: ${nForm}!")
                        Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show()
                        nForm.close()
                        finish()
                    }
                } else {
                    ndefTag.connect()
                    ndefTag.writeNdefMessage(message)
                    Log.i(TAG, "writeTag: success!")
                    Toast.makeText(this, "Successfully written to the NFC Tag!", Toast.LENGTH_SHORT).show()
                    ndefTag.close()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

}