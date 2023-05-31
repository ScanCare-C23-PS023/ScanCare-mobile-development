package com.maxisud.scancare.ui.home

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxisud.scancare.R
import com.maxisud.scancare.data.response.ProductResponseItem
import com.maxisud.scancare.databinding.FragmentHomeBinding
import com.synnapps.carouselview.ImageListener

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



        homeViewModel.products.observe(viewLifecycleOwner){product ->
            setProductsData(product)
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
        binding.rvRecommended.adapter = adapter
    }
}