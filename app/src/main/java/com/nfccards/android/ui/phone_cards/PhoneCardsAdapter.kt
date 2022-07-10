package com.nfccards.android.ui.phone_cards

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.nfccards.android.R
import com.nfccards.android.databinding.CardBusinessBinding
import com.nfccards.android.databinding.CardBusinessLogoBinding
import com.nfccards.android.databinding.CardSpotifyBinding
import com.nfccards.android.databinding.RvPhoneCardBinding
import com.nfccards.android.model.BusinessLogoModel
import com.nfccards.android.model.BusinessModel
import com.nfccards.android.model.CardTypeInterface
import com.nfccards.android.resources.CardType
import com.nfccards.android.ui.viewer.BusinessCardLogoActivity
import com.nfccards.android.ui.viewer.SingleLinkTypes
import com.nfccards.android.ui.viewer.SpotifyAndYoutubeCardActivity
import kotlin.coroutines.coroutineContext

class PhoneCardsAdapter(
    val activeCardId: String? = ""
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = ArrayList<CardTypeInterface>()
    private lateinit var mContext: Context

    inner class ViewHolder(val binding: RvPhoneCardBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceType")
        fun bind(position: Int) {
            val item = list[position]
            when(item.type){
                CardType.BUSINESS_NORMAL -> {
                    binding.card.viewStub?.let {
                        it.layoutResource = R.layout.card_business
                        it.inflate()

                        binding.txtTitle.text = "Normal Business Card"
                        binding.txtClickToEdit.setOnClickListener {
                            Toast.makeText(mContext, "Wait Bitch!", Toast.LENGTH_SHORT).show()
                        }

                        val active = (item as BusinessModel).id == activeCardId
                        binding.txtDefaultSharing.isVisible = active
                        binding.btnMakeDefault.setOnClickListener {

                        }
                    }
                }
                CardType.BUSINESS_LOGO -> {
                    binding.card.viewStub?.let {
                        it.layoutResource = R.layout.card_business_logo
                        val cardBinding = CardBusinessLogoBinding.inflate(it.layoutInflater)
                        it.inflate()

                        binding.txtTitle.text = "Mvp Business Card"
                        binding.txtClickToEdit.setOnClickListener {
                            val intent = Intent(mContext, BusinessCardLogoActivity::class.java)
                            intent.putExtra("isReader", false)
                            intent.putExtra("data", Gson().toJson(item))
                            mContext.startActivity(intent)
                        }

                        val active = (item as BusinessModel).id == activeCardId
                        binding.txtDefaultSharing.isVisible = active
                        binding.btnMakeDefault.isVisible = !active
                        binding.btnMakeDefault.setOnClickListener {
                            val db = Firebase.firestore

                            db.collection("user")
                                
                        }
                    }
                }
                CardType.BUSINESS_BACKGROUND -> {
                    binding.card.viewStub?.let {
                        it.layoutResource = R.layout.card_business
                        val cardBinding = CardBusinessBinding.inflate(it.layoutInflater)
                        it.inflate()

                        binding.txtTitle.text = "Colored Business Card"

                    }
                }
                CardType.SPOTIFY -> {
                    binding.card.viewStub?.let {
                        it.layoutResource = R.layout.card_spotify
                        val cardBinding = CardSpotifyBinding.inflate(it.layoutInflater)
                        it.inflate()

                        binding.txtTitle.text = "Spotify Card Playlist"
                        binding.txtClickToEdit.setOnClickListener {
                            val intent = Intent(mContext, SpotifyAndYoutubeCardActivity::class.java)
                            intent.putExtra("isReader", false)
                            intent.putExtra("SingleLinkTypes", SingleLinkTypes.SPOTIFY.toString())
                            mContext.startActivity(intent)
                        }
                    }
                }
                CardType.YOUTUBE -> {
                    binding.card.viewStub?.let {
                        it.layoutResource = R.layout.card_spotify
                        val view = it.inflate()

                        view.findViewById<RelativeLayout>(R.id.rlCard).setBackgroundColor(Color.Black.toArgb())
                        view.findViewById<ImageView>(R.id.imgCard).setImageResource(R.drawable.youtube_logo)
                        binding.txtTitle.text = "Youtube Link Card"
                        binding.txtClickToEdit.setOnClickListener {
                            val intent = Intent(mContext, SpotifyAndYoutubeCardActivity::class.java)
                            intent.putExtra("isReader", false)
                            intent.putExtra("SingleLinkTypes", SingleLinkTypes.YOUTUBE.toString())
                            mContext.startActivity(intent)
                        }
                    }
                }
                CardType.API -> {
                    binding.card.viewStub?.let {
                        it.layoutResource = R.layout.card_spotify
                        val view = it.inflate()

                        view.findViewById<RelativeLayout>(R.id.rlCard).setBackgroundColor(Color.Gray.toArgb())
                        view.findViewById<ImageView>(R.id.imgCard).setImageResource(R.drawable.api_logo)
                        binding.txtTitle.text = "API Card(Post Request)"
                        binding.txtClickToEdit.setOnClickListener {
                            val intent = Intent(mContext, SpotifyAndYoutubeCardActivity::class.java)
                            intent.putExtra("isReader", false)
                            intent.putExtra("SingleLinkTypes", SingleLinkTypes.API.toString())
                            mContext.startActivity(intent)
                        }

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_phone_card,
                parent,
                false
            )
        )
    }

    fun setList(items: ArrayList<CardTypeInterface>){
        list = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = CardType.getCardModel(list[position].type)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(position)
    }
}