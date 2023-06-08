package com.maxisud.scancare.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DiseaseProductResponse(

	@field:SerializedName("brand3")
	val brand3: List<Brand3Item>,

	@field:SerializedName("brand2")
	val brand2: List<Brand2Item>,

	@field:SerializedName("brand1")
	val brand1: List<Brand1Item>
) : Parcelable

@Parcelize
data class Brand3Item(

	@field:SerializedName("bgColor")
	val bgColor: String,

	@field:SerializedName("kind")
	val kind: String,

	@field:SerializedName("imageURL")
	val imageURL: String,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("brand")
	val brand: String,

	@field:SerializedName("desc")
	val desc: String
) : Parcelable

@Parcelize
data class Brand2Item(

	@field:SerializedName("bgColor")
	val bgColor: String,

	@field:SerializedName("kind")
	val kind: String,

	@field:SerializedName("imageURL")
	val imageURL: String,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("brand")
	val brand: String,

	@field:SerializedName("desc")
	val desc: String
) : Parcelable

@Parcelize
data class Brand1Item(

	@field:SerializedName("bgColor")
	val bgColor: String,

	@field:SerializedName("kind")
	val kind: String,

	@field:SerializedName("imageURL")
	val imageURL: String,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("brand")
	val brand: String,

	@field:SerializedName("desc")
	val desc: String
) : Parcelable
