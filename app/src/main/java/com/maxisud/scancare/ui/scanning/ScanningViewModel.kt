package com.maxisud.scancare.ui.scanning

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.maxisud.scancare.data.response.PredictionResponseItem
import com.maxisud.scancare.data.response.ProductResponseItem
import com.maxisud.scancare.data.retrofit.ApiConfig
import com.maxisud.scancare.ui.SharedRepository
import com.maxisud.scancare.ui.home.HomeViewModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanningViewModel : ViewModel() {
    private val _prediction = MutableLiveData<List<PredictionResponseItem>>()
    val prediction: LiveData<List<PredictionResponseItem>> = _prediction

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _croppedImageUri = MutableLiveData<Uri>()
    val croppedImageUri: LiveData<Uri> = _croppedImageUri

    fun uploadImage(imageMultiPart: MultipartBody.Part){
        _isLoading.value = true
        val apiService = ApiConfig.getFlaskApiService()
        val uploadImageRequest = apiService.uploadImage(imageMultiPart)
        uploadImageRequest.enqueue(object : Callback<List<PredictionResponseItem>> {
            override fun onResponse(
                call: Call<List<PredictionResponseItem>>,
                response: Response<List<PredictionResponseItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        SharedRepository.setPrediction(responseBody)
                        Log.d(TAG, "onResponse: Products loaded successfully: ${Gson().toJson(responseBody)}")
                    } else {
                        Log.e(TAG, "Response body is null")
                    }
                } else {
                    Log.e(TAG, "Api request failed with code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<PredictionResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(HomeViewModel.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setCroppedImageUri(uri: Uri) {
        _croppedImageUri.value = uri
    }
    companion object {
        private const val TAG = "ScanningViewModel"
    }
}