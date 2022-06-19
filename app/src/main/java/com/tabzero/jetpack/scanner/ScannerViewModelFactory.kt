package com.tabzero.jetpack.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ScannerViewModelFactory(
    private val isReader: Boolean,
    private val data: String,
    private val edit: Boolean
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScannerViewModel::class.java)){
            return ScannerViewModel(isReader = isReader, data = data, edit = edit) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}