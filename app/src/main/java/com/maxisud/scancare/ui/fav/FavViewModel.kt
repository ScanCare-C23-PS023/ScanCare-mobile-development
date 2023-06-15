package com.maxisud.scancare.ui.dashboard

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maxisud.scancare.data.response.ProductDetailResponseItem

class FavViewModel : ViewModel() {

    private val _favorites = MutableLiveData<List<ProductDetailResponseItem>>()
    val favorites: LiveData<List<ProductDetailResponseItem>> get() = _favorites

    fun fetchFavorites(userId: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("favorites/$userId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val favoriteList = mutableListOf<ProductDetailResponseItem>()
                dataSnapshot.children.forEach { snapshot ->
                    val productData = snapshot.getValue(ProductDetailResponseItem::class.java)
                    if (productData != null) {
                        favoriteList.add(productData)
                    }
                }
                _favorites.value = favoriteList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "fetchFavorites:onCancelled", databaseError.toException())
            }
        })
    }
}