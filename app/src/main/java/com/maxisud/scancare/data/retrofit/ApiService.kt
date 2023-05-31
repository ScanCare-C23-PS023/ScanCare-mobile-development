package com.maxisud.scancare.data.retrofit

import com.maxisud.scancare.data.response.ProductResponse
import com.maxisud.scancare.data.response.ProductResponseItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("product")
    fun getRecProduct(): Call<List<ProductResponseItem>>
}