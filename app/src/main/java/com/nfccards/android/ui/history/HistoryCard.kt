package com.nfccards.android.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nfccards.android.R
import com.nfccards.android.databinding.RcHistoryCardItemBinding
import com.nfccards.android.model.BusinessModel

class HistoryCard : RecyclerView.Adapter<HistoryCard.ViewHolder>(){

    inner class ViewHolder(binding: RcHistoryCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val item = diffUtils.currentList[position]


        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
        = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.rc_history_card_item, parent, false))

    override fun getItemCount(): Int = diffUtils.currentList.size

    private val diffUtilsCallBack = object: DiffUtil.ItemCallback<BusinessModel>(){
        override fun areItemsTheSame(oldItem: BusinessModel, newItem: BusinessModel): Boolean
            = oldItem.business == newItem.business

        override fun areContentsTheSame(oldItem: BusinessModel, newItem: BusinessModel): Boolean
            = oldItem == newItem
    }
    var diffUtils = AsyncListDiffer(this, diffUtilsCallBack)
}