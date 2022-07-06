package com.tabzero.jetpack.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessModel(
    val name: String,
    val business: String,
    val phoneNum: String,
    val webSite: String,
)
