package com.maxisud.scancare.ui.home

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maxisud.scancare.R
import com.maxisud.scancare.data.response.ProductResponseItem

class ProductAdapter(private val listProduct: List<ProductResponseItem>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val TAG = "ProductAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_recommended, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder at position $position")
        val product = listProduct[position]

        holder.tvProduct.text = product.product.trimIndent()
        holder.tvBrand.text = product.brand.trimIndent()
        holder.tvRating.text = product.rating.trimIndent()
        val color = Color.parseColor(product.bgColor)
        holder.cardView.setCardBackgroundColor(color)

        Glide.with(holder.itemView.context)
            .load(product.imgUrl)
            .into(holder.imgProduct)
        holder.itemView.setOnClickListener{ onItemClickCallback.onItemClicked(product)}
        Log.d(TAG, "onBindViewHolder tvproduct ${holder.tvProduct}")
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${listProduct.size}")
        return listProduct.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvBrand: TextView = view.findViewById(R.id.tv_brandname_recommended)
        val tvProduct: TextView = view.findViewById(R.id.tv_productNameRecommended)
        val tvRating: TextView = view.findViewById(R.id.tv_rating)
        val imgProduct: ImageView = view.findViewById(R.id.img_product)
        val cardView: CardView = view.findViewById(R.id.cardview_products)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(product: ProductResponseItem)
    }
}