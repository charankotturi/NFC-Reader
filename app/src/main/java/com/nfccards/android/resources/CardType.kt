package com.nfccards.android.resources

import com.nfccards.android.model.BusinessModel

enum class CardType {
    BUSINESS_NORMAL,
    BUSINESS_LOGO,
    BUSINESS_BACKGROUND;

    companion object {
        fun getCardModel(cardType: CardType) : Class<*> {
            return when(cardType){
                BUSINESS_NORMAL -> BusinessModel::class.java
                BUSINESS_LOGO -> BusinessModel::class.java
                BUSINESS_BACKGROUND -> BusinessModel::class.java
            }
        }
    }
}