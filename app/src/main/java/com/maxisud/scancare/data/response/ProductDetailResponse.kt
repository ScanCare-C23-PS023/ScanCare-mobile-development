package com.maxisud.scancare.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ProductDetailResponse(

	@field:SerializedName("ProductDetailResponse")
	val productDetailResponse: List<ProductDetailResponseItem>
) : Parcelable

@Parcelize
data class ProductDetailResponseItem(

	@field:SerializedName("bgColor")
	val bgColor: String = "",

	@field:SerializedName("kind")
	val kind: String = "",

	@field:SerializedName("imageURL")
	val imageURL: String = "",

	@field:SerializedName("rating")
	val rating: String = "",

	@field:SerializedName("ingredients")
	val ingredients: String = "",

	@field:SerializedName("id")
	val id: String = "",

	@field:SerializedName("time")
	val time: String = "",

	@field:SerializedName("brand")
	val brand: String = "",

	@field:SerializedName("desc")
	val desc: String = "",

	@SerializedName("isFavorite")
	var isFavorite: Boolean? = null
) : Parcelable
