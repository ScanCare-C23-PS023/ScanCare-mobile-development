package com.maxisud.scancare.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ArticlesResponse(

	@field:SerializedName("ArticlesResponse")
	val articlesResponse: List<ArticlesResponseItem>
) : Parcelable

@Parcelize
data class ArticlesResponseItem(

	@field:SerializedName("img_url")
	val imgUrl: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("headline")
	val headline: String
) : Parcelable
