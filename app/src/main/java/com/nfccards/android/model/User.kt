package com.nfccards.android.model

import com.nfccards.android.resources.CardType
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val phoneNum: String,
    val username: String,
    var activeCardId: String? = ""
)
