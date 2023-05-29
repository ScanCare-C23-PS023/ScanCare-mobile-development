package com.maxisud.scancare.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maxisud.scancare.R
import com.maxisud.scancare.databinding.FragmentHomeBinding
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    // Initialize the images
    private val sampleImages = intArrayOf(R.drawable.banner_carousel, R.drawable.banner_carousel, R.drawable.banner_carousel)

    private val imageListener = ImageListener { position, imageView ->
        imageView.setImageResource(sampleImages[position])
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.text.observe(viewLifecycleOwner) {

        }

        // Initialize the CarouselView
        binding.carouselView.setPageCount(sampleImages.size)
        binding.carouselView.setImageListener(imageListener)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}