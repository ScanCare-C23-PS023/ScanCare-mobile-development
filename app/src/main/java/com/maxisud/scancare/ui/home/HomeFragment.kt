package com.maxisud.scancare.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maxisud.scancare.R
import com.maxisud.scancare.databinding.FragmentHomeBinding
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())

    private var currentState = 0

    private val slideRunnable: Runnable = object : Runnable {
        override fun run() {
            when (currentState) {
                0 -> {
                    _binding?.constraintLayout?.setTransition(R.id.menu1, R.id.menu2)
                    currentState = 1
                }
                1 -> {
                    _binding?.constraintLayout?.setTransition(R.id.menu2, R.id.menu3)
                    currentState = 2
                }
                2 -> {
                    _binding?.constraintLayout?.setTransition(R.id.menu3, R.id.menu1)
                    currentState = 0
                }
            }
            _binding?.constraintLayout?.transitionToEnd()

            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.text.observe(viewLifecycleOwner) {

        }

        return root
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
}