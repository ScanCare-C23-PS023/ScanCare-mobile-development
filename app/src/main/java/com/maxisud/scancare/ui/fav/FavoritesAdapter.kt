package com.maxisud.scancare.ui.fav

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maxisud.scancare.R
import com.maxisud.scancare.data.response.ProductDetailResponseItem
import com.maxisud.scancare.ui.product_detail.ProductDetailActivity

class FavoritesAdapter(
    var items: List<ProductDetailResponseItem>,
    private val context: Context
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardViewProducts: CardView = view.findViewById(R.id.cardview_products)
        val imgProduct: ImageView = view.findViewById(R.id.img_product)
        val tvBrandName: TextView = view.findViewById(R.id.tv_brandname_recommended)
        val tvProductName: TextView = view.findViewById(R.id.tv_productNameRecommended)
        val tvRating: TextView = view.findViewById(R.id.tv_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_recommended, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val product = items[position]
        holder.cardViewProducts.setCardBackgroundColor(Color.parseColor(product.bgColor))
        holder.tvBrandName.text = product.brand
        holder.tvProductName.text = product.kind
        holder.tvRating.text = product.rating
        Glide.with(holder.imgProduct.context)
            .load(product.imageURL)
            .into(holder.imgProduct)

        holder.cardViewProducts.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(ProductDetailActivity.EXTRA_DATA, product.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}