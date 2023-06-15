package com.maxisud.scancare.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.maxisud.scancare.data.response.ArticlesResponseItem
import com.maxisud.scancare.data.response.PredictionResponseItem
import com.maxisud.scancare.data.response.ProductResponseItem
import com.maxisud.scancare.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _products = MutableLiveData<List<ProductResponseItem>>()
    val products: LiveData<List<ProductResponseItem>> = _products

    private val _articles = MutableLiveData<List<ArticlesResponseItem>>()
    val articles: LiveData<List<ArticlesResponseItem>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        accessFlaskApi()
        findProducts()
        findArticles()
    }

    private fun accessFlaskApi() {
        val apiService = ApiConfig.getFlaskApiService()
        val call = apiService.uploadImage(createDummyRequestBody())

        call.enqueue(object : Callback<List<PredictionResponseItem>> {
            override fun onResponse(
                call: Call<List<PredictionResponseItem>>,
                response: Response<List<PredictionResponseItem>>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Flask API accessed successfully")
                } else {
                    Log.e(TAG, "Failed to access Flask API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<PredictionResponseItem>>, t: Throwable) {
                Log.e(TAG, "Failed to access Flask API: ${t.message}")
            }
        })
    }

    fun findProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getRecProduct()
            client.enqueue(object : Callback<List<ProductResponseItem>> {
                override fun onResponse(
                    call: Call<List<ProductResponseItem>>,
                    response: Response<List<ProductResponseItem>>
                ) {
                    _isLoading.value = false
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
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    fun findArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getArticles()
            client.enqueue(object : Callback<List<ArticlesResponseItem>> {
                override fun onResponse(
                    call: Call<List<ArticlesResponseItem>>,
                    response: Response<List<ArticlesResponseItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _articles.value = responseBody
                            Log.d(TAG, "onResponse: articles loaded successfully: ${Gson().toJson(responseBody)}")
                        } else {
                            Log.e(TAG, "Response body is null")
                        }
                    } else {
                        Log.e(TAG, "Api request failed with code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<ArticlesResponseItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    private fun createDummyRequestBody(): MultipartBody.Part {
        val requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), "dummy image data")
        return MultipartBody.Part.createFormData("image", "dummy.jpg", requestBody)
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}