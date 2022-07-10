package com.nfccards.android.resources

import com.google.gson.Gson
import com.nfccards.android.model.BusinessLogoModel
import com.nfccards.android.model.BusinessModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Utils {
    companion object {
        fun getBusinessModel(data: String) : BusinessModel {
            if (data == ""){
                val model = BusinessModel(
                    name = "no data",
                    business = "no data",
                    phoneNum = "no data",
                    webSite = "no data" )
                return model
            }
            val dataArray = data.split(",")
            val model = BusinessModel(
                name = dataArray[0],
                business = dataArray[1],
                phoneNum = dataArray[2],
                webSite = dataArray[3],
                id = dataArray[4]
            )
            return model
        }

        fun getBusinessLogoModel(data: String) : BusinessLogoModel {
            return Gson().fromJson(data, BusinessLogoModel::class.java)
        }
    }
}