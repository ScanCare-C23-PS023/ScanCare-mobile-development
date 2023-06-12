package com.maxisud.scancare.ui.home

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxisud.scancare.R
import com.maxisud.scancare.data.response.ArticlesResponseItem
import com.maxisud.scancare.data.response.ProductResponseItem
import com.maxisud.scancare.databinding.FragmentHomeBinding
import com.maxisud.scancare.ui.product_detail.ProductDetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory()
    }
    private val handler = Handler(Looper.getMainLooper())
    private var currentState = 0
    private val slideRunnable: Runnable = object : Runnable {
        override fun run() {
            when (currentState) {
                0 -> {
                    _binding?.constraintLayout?.transitionToState(R.id.menu2)
                    currentState = 1
                }
                1 -> {
                    _binding?.constraintLayout?.transitionToState(R.id.menu3)
                    currentState = 2
                }
                2 -> {
                    _binding?.constraintLayout?.transitionToState(R.id.menu1)
                    currentState = 0
                }
            }
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvRecommended.layoutManager = layoutManager
        val layoutManagerArticles = LinearLayoutManager(context)
        binding.rvArticles.layoutManager = layoutManagerArticles


        homeViewModel.products.observe(viewLifecycleOwner){product ->
            setProductsData(product)
        }

        homeViewModel.articles.observe(viewLifecycleOwner){article ->
            setArticlesData(article)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler.postDelayed(slideRunnable, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(slideRunnable)
        _binding = null
    }

    private fun setProductsData(products: List<ProductResponseItem>) {
        val adapter = ProductAdapter(products)
        Log.d(ContentValues.TAG, "products count in Activity: ${products.size}")
        binding.rvRecommended.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback{
            override fun onItemClicked(product: ProductResponseItem){
                val intentToDetail = Intent(requireContext(), ProductDetailActivity::class.java)
                intentToDetail.putExtra("DATA", product.id)
                startActivity(intentToDetail)
            }
        })
        binding.rvRecommended.adapter = adapter
    }

    private fun setArticlesData(articles: List<ArticlesResponseItem>) {
        val adapter = ArticlesAdapter(articles)
        Log.d(ContentValues.TAG, "article count in Activity: ${articles.size}")
        adapter.setOnItemClickCallback(object : ArticlesAdapter.OnItemClickCallback {
            override fun onItemClicked(article: ArticlesResponseItem) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(article.link)
                startActivity(intent)
            }
        })
        binding.rvArticles.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.loadingGif.visibility = View.VISIBLE
        } else {
            binding.loadingGif.visibility = View.GONE
        }
    }
}
