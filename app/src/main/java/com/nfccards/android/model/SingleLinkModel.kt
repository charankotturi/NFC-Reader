package com.nfccards.android.model

import com.nfccards.android.resources.CardType

data class SingleLinkModel (
    val id: String? = null,
    val name: String,
    val userPhoneNum: String,
    val url: String,
    override val type: CardType = CardType.API
) : CardTypeInterface