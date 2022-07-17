package com.nfccards.android.resources

import com.google.gson.Gson
import com.nfccards.android.model.BusinessLogoModel
import com.nfccards.android.model.BusinessModel
import java.lang.Exception

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
            try {
                return BusinessModel(
                    name = dataArray[0],
                    business = dataArray[1],
                    phoneNum = dataArray[2],
                    webSite = dataArray[3],
                    id = dataArray[4]
                )
            } catch (e: Exception) {
                return BusinessModel(
                    name = dataArray[0],
                    business = dataArray[1],
                    phoneNum = dataArray[2],
                    webSite = dataArray[3],
                )
            }
        }

        fun getBusinessLogoModel(data: String) : BusinessLogoModel {
            return Gson().fromJson(data, BusinessLogoModel::class.java)
        }
    }
}