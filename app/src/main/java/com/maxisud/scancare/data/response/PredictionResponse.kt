package com.maxisud.scancare.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PredictionResponse(

	@field:SerializedName("PredictionResponse")
	val predictionResponse: List<PredictionResponseItem?>? = null
) : Parcelable

@Parcelize
data class PredictionResponseItem(

	@field:SerializedName("bg_color")
	val bgColor: String? = null,

	@field:SerializedName("percentage")
	val percentage: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) : Parcelable
