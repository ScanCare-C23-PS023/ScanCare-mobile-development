package com.maxisud.scancare.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxisud.scancare.data.response.PredictionResponseItem

object SharedRepository {
    private val _prediction = MutableLiveData<List<PredictionResponseItem>?>()
    val prediction: LiveData<List<PredictionResponseItem>?> = _prediction

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    val _isTimeout = MutableLiveData<Boolean>()
    val isTimeout: LiveData<Boolean> = _isTimeout

    fun setPrediction(prediction: List<PredictionResponseItem>) {
        _prediction.value = prediction
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun clear() {
        _prediction.value = null
        _isLoading.value = null
        _isTimeout.value = false
    }

    fun setTimeout(isTimeout: Boolean) {
        _isTimeout.value = isTimeout
    }
}
