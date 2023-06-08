package com.maxisud.scancare.ui.product_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.maxisud.scancare.R
import com.maxisud.scancare.data.response.DiseaseDetailResponse
import com.maxisud.scancare.data.response.ProductDetailResponseItem
import com.maxisud.scancare.databinding.ActivityProductDetailBinding

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

        if (id != null) {
            productDetailViewModel.getDetailProduct(id!!)
        }

        productDetailViewModel.detailProduct.observe(this){id ->
            setDetailProduct(id)
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

    companion object{
        const val EXTRA_DATA = "DATA"
    }
}