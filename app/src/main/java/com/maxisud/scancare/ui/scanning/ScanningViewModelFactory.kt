package com.maxisud.scancare.ui.scanning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxisud.scancare.ui.home.HomeViewModel

class ScanningViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanningViewModel::class.java)) {
            return ScanningViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}