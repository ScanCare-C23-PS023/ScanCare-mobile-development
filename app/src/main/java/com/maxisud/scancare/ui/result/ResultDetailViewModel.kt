package com.maxisud.scancare.ui.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.maxisud.scancare.data.response.DiseaseDetailResponse
import com.maxisud.scancare.data.response.ProductResponseItem
import com.maxisud.scancare.data.retrofit.ApiConfig
import com.maxisud.scancare.ui.SharedRepository
import com.maxisud.scancare.ui.home.HomeViewModel
import com.maxisud.scancare.ui.product_detail.ProductDetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultDetailViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _products = MutableLiveData<List<ProductResponseItem>>()
    val products: LiveData<List<ProductResponseItem>> = _products

    private val _detailDisease = MutableLiveData<DiseaseDetailResponse>()
    val detailDisease: LiveData<DiseaseDetailResponse> = _detailDisease

    val fragmentState = MutableLiveData<Int>()

    fun getDetailDisease(nameDisease: String){
        checkForTimeout(5000)
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
                            Log.e(TAG, "Response body is null")
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

    fun findProductsDisease(name: String) {
        checkForTimeout(5000)
        viewModelScope.launch {
            val client = ApiConfig.getApiService().getRecProductDisease(name)
            Log.d(TAG, "API request URL: ${client.request().url}")
            client.enqueue(object : Callback<List<ProductResponseItem>> {
                override fun onResponse(
                    call: Call<List<ProductResponseItem>>,
                    response: Response<List<ProductResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _products.value = responseBody
                            Log.d(TAG, "onResponse: Products loaded successfully: ${Gson().toJson(responseBody)}")
                        } else {
                            Log.e(TAG, "Response body is null")
                        }
                    } else {
                        Log.e(TAG, "Api request failed with code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<ProductResponseItem>>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    fun checkForTimeout(time: Long) {
        viewModelScope.launch {
            delay(time)
            if (_products.value == null || _detailDisease.value == null) {
                SharedRepository._isTimeout.value = true
            }
        }
    }



    companion object{
        private const val TAG = "ResultDetailViewModel"
    }
}