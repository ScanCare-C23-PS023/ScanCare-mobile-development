package com.maxisud.scancare.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxisud.scancare.data.response.PredictionResponseItem

object SharedRepository {
    private val _prediction = MutableLiveData<List<PredictionResponseItem>>()
    val prediction: LiveData<List<PredictionResponseItem>> = _prediction

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setPrediction(prediction: List<PredictionResponseItem>) {
        _prediction.value = prediction
        _isLoading.value = false
    }
}
