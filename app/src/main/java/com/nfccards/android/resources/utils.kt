package com.nfccards.android.resources

import com.nfccards.android.model.BusinessModel

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
                webSite = dataArray[3]
            )
            return model
        }
    }
}