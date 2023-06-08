package com.maxisud.scancare.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ProductResponse(

	@field:SerializedName("ProductResponse")
	val productResponse: List<ProductResponseItem>
) : Parcelable

@Parcelize
data class ProductResponseItem(

	@field:SerializedName("bgColor")
	val bgColor: String,

	@field:SerializedName("kind")
	val kind: String,

	@field:SerializedName("imageURL")
	val imgUrl: String,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("brand")
	val brand: String
) : Parcelable
