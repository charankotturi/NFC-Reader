package com.nfccards.android.ui.history

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nfccards.android.R
import com.nfccards.android.databinding.RcHistoryCardItemBinding
import com.nfccards.android.model.BusinessModel

class HistoryCard : RecyclerView.Adapter<HistoryCard.ViewHolder>(){

    private lateinit var mContext: Context

    inner class ViewHolder(val binding: RcHistoryCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val item = diffUtils.currentList[position]

            binding.card.txtName.text = item.name
            binding.card.txtBusinessName.text = item.business
            binding.card.txtWebsite.text = item.webSite
            binding.card.txtPhoneNum.text = item.phoneNum

            if (item.webSite.isEmpty()){
                binding.card.llWebsite.visibility = View.GONE
            }

            if (item.phoneNum.isEmpty()){
                binding.card.llWebsite.visibility = View.GONE
            }

            // To open website from the business card
            binding.card.llWebsite.setOnClickListener {
                try {
                    val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.webSite))
                    mContext.startActivity(myIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        mContext, "something went wrong!", Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                }
            }

            // To open phone app from the business card
            binding.card.llPhoneNumber.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${item.phoneNum}")
                mContext.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
        = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.rc_history_card_item, parent, false)) }

    override fun getItemCount(): Int = diffUtils.currentList.size

    private val diffUtilsCallBack = object: DiffUtil.ItemCallback<BusinessModel>(){
        override fun areItemsTheSame(oldItem: BusinessModel, newItem: BusinessModel): Boolean
            = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BusinessModel, newItem: BusinessModel): Boolean
            = oldItem == newItem
    }
    var diffUtils = AsyncListDiffer(this, diffUtilsCallBack)
}