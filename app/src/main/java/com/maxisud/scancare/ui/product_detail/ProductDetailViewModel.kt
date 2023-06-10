package com.maxisud.scancare.ui.product_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.maxisud.scancare.data.response.ProductDetailResponseItem
import com.maxisud.scancare.data.response.ProductResponseItem
import com.maxisud.scancare.data.retrofit.ApiConfig
import com.maxisud.scancare.ui.home.HomeViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailViewModel : ViewModel() {

    private val _detailProduct = MutableLiveData<ProductDetailResponseItem>()
    val detailProduct: LiveData<ProductDetailResponseItem> = _detailProduct

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading




    fun getDetailProduct(idProduct: String){
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getDetailProduct(idProduct)
            Log.d(TAG, "API request URL: ${client.request().url}")
            client.enqueue(object : Callback<List<ProductDetailResponseItem>> {
                override fun onResponse(
                    call: Call<List<ProductDetailResponseItem>>,
                    response: Response<List<ProductDetailResponseItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if (responseBody != null && responseBody.isNotEmpty()){
                            _detailProduct.value = responseBody[0]
                        } else {
                            Log.e(TAG, "Response body is null")
                        }
                    } else {
                        Log.e(TAG, "Api request failed with code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<ProductDetailResponseItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    companion object{
        private const val TAG = "ProductDetailViewModel"
    }
}