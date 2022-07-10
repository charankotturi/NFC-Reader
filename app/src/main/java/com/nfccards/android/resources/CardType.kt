package com.nfccards.android.resources

import com.nfccards.android.model.BusinessModel

enum class CardType {
    BUSINESS_NORMAL,
    BUSINESS_LOGO,
    BUSINESS_BACKGROUND,
    SPOTIFY,
    YOUTUBE,
    API;

    companion object {
        fun getCardModel(cardType: CardType) : Int {
            return when(cardType){
                BUSINESS_NORMAL -> 1001
                BUSINESS_LOGO -> 1002
                BUSINESS_BACKGROUND -> 1003
                SPOTIFY -> 1004
                YOUTUBE -> 1005
                API -> 1006
            }
        }
    }
}