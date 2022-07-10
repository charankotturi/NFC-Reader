package com.nfccards.android.ui.phone_cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.nfccards.android.R
import com.nfccards.android.databinding.ActivityPhoneBinding
import com.nfccards.android.model.BusinessLogoModel
import com.nfccards.android.model.BusinessModel
import com.nfccards.android.model.CardTypeInterface
import com.nfccards.android.model.SingleLinkModel
import com.nfccards.android.resources.CardType

class PhoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone)
        val list = ArrayList<CardTypeInterface>()
        list.add(
            BusinessLogoModel(
                name = "Kotturi",
                business = "Start-Up",
                phoneNum = "23232323",
                webSite = "",
                position = "founder",
                userPhoneNumber = "32423432432",
                logoUrl = "somehting useless",
            )
        )
        list.add(
            SingleLinkModel(
                name = "Kotturi",
                url = "",
                userPhoneNum = "",
                type = CardType.SPOTIFY,
            )
        )
        list.add(
            SingleLinkModel(
                name = "Charan",
                url = "",
                userPhoneNum = "",
                type = CardType.YOUTUBE,
            )
        )
        list.add(
            SingleLinkModel(
                name = "Ajay",
                url = "",
                userPhoneNum = "",
                type = CardType.API,
            )
        )

        val adapter = PhoneCardsAdapter()
        adapter.setList(list)
        binding.rvCardFlavours.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@PhoneActivity, LinearLayoutManager.HORIZONTAL,false)
        }

        PagerSnapHelper().attachToRecyclerView(binding.rvCardFlavours)
    }
}