package com.maxisud.scancare.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maxisud.scancare.R
import com.maxisud.scancare.data.response.ArticlesResponseItem
import com.maxisud.scancare.data.response.ProductResponseItem

class ArticlesAdapter(private val listArticle: List<ArticlesResponseItem>) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val TAG = "ArticlesAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_articles, parent, false)
        )
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${listArticle.size}")
        return listArticle.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder at position $position")
        val article = listArticle[position]

        holder.tvArticle.text = article.headline.trimIndent()
        Glide.with(holder.itemView.context)
            .load(article.imgUrl)
            .into(holder.imgArticle)
        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(article)}

    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvArticle: TextView = view.findViewById(R.id.tv_title_article)
        val imgArticle: ImageView = view.findViewById(R.id.article_image)
    }

    interface OnItemClickCallback{
        fun onItemClicked(article: ArticlesResponseItem)
    }
}