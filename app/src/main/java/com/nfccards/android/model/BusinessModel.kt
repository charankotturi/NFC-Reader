package com.nfccards.android.model

import com.nfccards.android.resources.CardType
import kotlinx.serialization.Serializable

@Serializable
data class BusinessModel(
    override val id: String? = null,
    val name: String,
    val business: String,
    val phoneNum: String,
    val webSite: String,
    override val type: CardType = CardType.BUSINESS_NORMAL,
) : CardTypeInterface
