package com.maxisud.scancare.ui.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxisud.scancare.data.response.DiseaseDetailResponse
import com.maxisud.scancare.data.retrofit.ApiConfig
import com.maxisud.scancare.ui.home.HomeViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultDetailViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailDisease = MutableLiveData<DiseaseDetailResponse>()
    val detailDisease: LiveData<DiseaseDetailResponse> = _detailDisease

    fun getDetailDisease(nameDisease: String){
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getDetailDisease(nameDisease)
            Log.d(TAG, "API request URL: ${client.request().url}")
            client.enqueue(object : Callback<DiseaseDetailResponse> {
                override fun onResponse(
                    call: Call<DiseaseDetailResponse>,
                    response: Response<DiseaseDetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if (responseBody != null){
                            _detailDisease.value = responseBody
                        } else {
                            Log.e(HomeViewModel.TAG, "Response body is null")
                        }
                    } else {
                        Log.e(TAG, "Api request failed with code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<DiseaseDetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(HomeViewModel.TAG, "onFailure: ${t.message}")
                }

            })
        }
    }



    companion object{
        private const val TAG = "ResultDetailViewModel"
    }
}