package com.maxisud.scancare.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DiseaseDetailResponse(

	@field:SerializedName("characteristics")
	val characteristics: String,

	@field:SerializedName("causes")
	val causes: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
) : Parcelable
