package com.nfccards.android.ui.phone_cards

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.SetOptions
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
import com.nfccards.android.model.User
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
            val db = Firebase.firestore
            val sharedPreferences: SharedPreferences = mContext.getSharedPreferences("MY_DB",
                AppCompatActivity.MODE_PRIVATE
            )!!
            val currentUserJson = sharedPreferences.getString("current_user", "")

            if (currentUserJson != ""){
                item.id?.let {
                }
            }

            when(item.type){
                CardType.BUSINESS_NORMAL -> {
                    binding.card.viewStub?.let {
                        it.layoutResource = R.layout.card_business
                        it.inflate()

                        binding.txtTitle.text = "Normal Business Card"
                        binding.txtClickToEdit.setOnClickListener {
                            Toast.makeText(mContext, "Wait Bitch!", Toast.LENGTH_SHORT).show()
                        }

                        val activeItem = (item as BusinessModel)
                        binding.btnMakeDefault.setOnClickListener {

                        }
                    }
                }
                CardType.BUSINESS_LOGO -> {
                    binding.card.viewStub?.let {
                        it.layoutResource = R.layout.card_business_logo
                        it.inflate()

                        binding.txtTitle.text = "Mvp Business Card"
                        binding.txtClickToEdit.setOnClickListener {
                            val intent = Intent(mContext, BusinessCardLogoActivity::class.java)
                            intent.putExtra("isReader", false)
                            intent.putExtra("data", Gson().toJson(item))
                            mContext.startActivity(intent)
                        }

                        val activeItem = (item as BusinessLogoModel)
                        it.findViewById<EditText>(R.id.txtBusinessName).setText(activeItem.business)
                        it.findViewById<EditText>(R.id.txtName).setText(activeItem.name)
                        it.findViewById<EditText>(R.id.txtPosition).setText(activeItem.position)

                        if (currentUserJson != "") {
                            val user = Gson().fromJson(currentUserJson, User::class.java)

                            binding.txtDefaultSharing.isVisible = item.id == user.activeCardId
                        }

                        binding.btnMakeDefault.setOnClickListener {
                            if (currentUserJson != ""){
                                val user = Gson().fromJson(currentUserJson, User::class.java)

                                item.id?.let{
                                    user.activeCardId = item.id
                                    val edit = sharedPreferences.edit()
                                    edit.apply {
                                        this.putString("current_user", Gson().toJson(user))
                                    }
                                    edit.commit()

                                    val userMap = {
                                        "phoneNum" to user.phoneNum
                                        "username" to user.username
                                        "activeCardId" to user.activeCardId
                                    }

                                    db.collection("user")
                                        .document(it)
                                        .get()
                                        .addOnSuccessListener { doc ->
                                            if (doc.exists()){
                                                db.collection("user")
                                                    .document(it)
                                                    .set(userMap ,SetOptions.merge())
                                                    .addOnSuccessListener {
                                                        Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show()
                                                    }
                                            }else{
                                                Toast.makeText(mContext, "Make sure the doc exists!", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                    (mContext as Activity).finish()
                                    return@setOnClickListener
                                }
                                Toast.makeText(mContext, "Edit the card first!", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(mContext, "Create a user first!", Toast.LENGTH_SHORT).show()
                            }
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