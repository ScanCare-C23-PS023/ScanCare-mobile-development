package com.maxisud.scancare.ui.product_detail

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.maxisud.scancare.ui.Login.LoginActivity
import com.maxisud.scancare.R
import com.maxisud.scancare.data.response.ProductDetailResponseItem
import com.maxisud.scancare.databinding.ActivityProductDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductDetailActivity : AppCompatActivity() {
    private var _binding: ActivityProductDetailBinding? = null
    private val binding get() = _binding!!
    private val productDetailViewModel by viewModels<ProductDetailViewModel> {
        ProductDetailViewModelFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra(EXTRA_DATA)
        val user = Firebase.auth.currentUser

        if (id != null) {
            productDetailViewModel.getDetailProduct(id!!)
        }

        productDetailViewModel.detailProduct.observe(this){product ->
            setDetailProduct(product)
            val user = Firebase.auth.currentUser
            if (user != null) {
                setFavoriteIcon(user.uid, product.id)
            }
        }

        productDetailViewModel.isLoading.observe(this){
            showLoading(it)
        }


        binding.iconFavorite.setOnClickListener {
            val user = Firebase.auth.currentUser
            if (user != null) {
                val product = productDetailViewModel.detailProduct.value
                if (product != null && id != null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        toggleFavoriteStatus(user.uid, id, product)
                    }
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java).apply {
                    putExtra(LoginActivity.EXTRA_PRODUCT_ID, id)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setDetailProduct(product: ProductDetailResponseItem){
        binding.productBrand.text = product.brand
        binding.productKind.text = product.kind
        binding.productDescription.text = product.desc
        binding.tvTimes.text = product.time
        binding.tvIngredients.text = product.ingredients
        Glide.with(this@ProductDetailActivity)
            .load(product.imageURL)
            .into(binding.imageProduct)
    }

    fun toggleFavoriteStatus(userId: String, productId: String, product: ProductDetailResponseItem) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("favorites/$userId/$productId")

        CoroutineScope(Dispatchers.IO).launch {
            val productData = ref.get().await().getValue(ProductDetailResponseItem::class.java)
            if (productData != null && productData.isFavorite == true) {
                ref.removeValue()
            } else {
                product.isFavorite = true
                ref.setValue(product)
            }

            withContext(Dispatchers.Main){
                setFavoriteIcon(userId, productId)
            }
        }
    }

    fun setFavoriteIcon(userId: String, productId: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("favorites/$userId/$productId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productData = dataSnapshot.getValue(ProductDetailResponseItem::class.java)
                if (productData != null && productData.isFavorite == true) {
                    binding.iconFavorite.setImageResource(R.drawable.ic_favorite_red_full_24dp)
                } else {
                    binding.iconFavorite.setImageResource(R.drawable.ic_favorite_red_border_24dp)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "setFavoriteIcon:onCancelled", databaseError.toException())
            }
        })
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object{
        const val EXTRA_DATA = "DATA"
    }
}