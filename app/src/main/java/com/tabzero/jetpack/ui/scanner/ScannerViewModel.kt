package com.tabzero.jetpack.ui.scanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tabzero.jetpack.model.BusinessModel

class ScannerViewModel(
    private val isReader: Boolean,
    private val data: String,
    private val edit: Boolean
) : ViewModel() {
    val isReadingCard = isReader
    val viewData = data
    val viewEdit = edit

    private val mutableBusinessModel = MutableLiveData<BusinessModel>()

    init {
        getBusinessModel(data)
    }

    private fun getBusinessModel(data: String) : BusinessModel {
        if (data == ""){
            val model = BusinessModel(
                name = "no data",
                business = "no data",
                phoneNum = "no data",
                webSite = "no data" )
            mutableBusinessModel.postValue(model)
            return model
        }
        val dataArray = data.split(",")
        val model = BusinessModel(
            name = dataArray[0],
            business = dataArray[1],
            phoneNum = dataArray[2],
            webSite = dataArray[3]
        )
        mutableBusinessModel.postValue(model)
        return model
    }
}