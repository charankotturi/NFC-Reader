package com.nfccards.android.model

import com.nfccards.android.resources.CardType
import kotlinx.serialization.Serializable

@Serializable
data class BusinessLogoModel (
    val id: String? = null,
    val name: String,
    val business: String,
    val phoneNum: String,
    val webSite: String,
    val position: String,
    val logoUrl: String,
    val userPhoneNumber: String,
    override val type: CardType = CardType.BUSINESS_LOGO
) : CardTypeInterface