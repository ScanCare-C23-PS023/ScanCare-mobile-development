package com.maxisud.scancare.data.retrofit

import com.maxisud.scancare.data.response.ArticlesResponseItem
import com.maxisud.scancare.data.response.DiseaseDetailResponse
import com.maxisud.scancare.data.response.PredictionResponse
import com.maxisud.scancare.data.response.PredictionResponseItem
import com.maxisud.scancare.data.response.ProductDetailResponseItem
import com.maxisud.scancare.data.response.ProductResponse
import com.maxisud.scancare.data.response.ProductResponseItem
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    fun getRecProduct(): Call<List<ProductResponseItem>>

    @GET("products/{id}")
    fun getDetailProduct(@Path("id") idProduct: String) : Call<List<ProductDetailResponseItem>>

    @GET("diseases/{name_disease}")
    fun getDetailDisease(@Path("name_disease") nameDisease: String) : Call<DiseaseDetailResponse>

    @GET("products/{name_disease}")
    fun getRecProductDisease(@Path("name_disease") nameDisease: String): Call<List<ProductResponseItem>>

    @GET("articles")
    fun getArticles(): Call<List<ArticlesResponseItem>>
}

interface FlaskApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(@Part image: MultipartBody.Part): Call<List<PredictionResponseItem>>
}