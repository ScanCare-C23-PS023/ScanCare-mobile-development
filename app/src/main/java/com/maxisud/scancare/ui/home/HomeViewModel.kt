package com.maxisud.scancare.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.maxisud.scancare.data.response.ProductResponse
import com.maxisud.scancare.data.response.ProductResponseItem
import com.maxisud.scancare.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _products = MutableLiveData<List<ProductResponseItem>>()
    val products: LiveData<List<ProductResponseItem>> = _products

    init {
        findProducts()
    }

    fun findProducts() {
        viewModelScope.launch {
            val client = ApiConfig.getApiService().getRecProduct()
            client.enqueue(object : Callback<List<ProductResponseItem>> {
                override fun onResponse(
                    call: Call<List<ProductResponseItem>>,
                    response: Response<List<ProductResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _products.value = responseBody
                            // Convert data to JSON string and print it
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

    companion object {
        private const val TAG = "HomeViewModel"
    }
}