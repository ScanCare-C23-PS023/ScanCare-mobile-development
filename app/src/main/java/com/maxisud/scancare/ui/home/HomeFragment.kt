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

    // Using nullable binding variable with backing property
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Handler for posting tasks to the main thread
    private val handler = Handler(Looper.getMainLooper())

    // Runnable for controlling the slide transitions
    private var currentState = 0

    // Runnable for controlling the slide transitions
    private val slideRunnable: Runnable = object : Runnable {
        override fun run() {
            // Transition to the next state
            when (currentState) {
                0 -> {
                    _binding?.constraintLayout?.setTransition(R.id.start, R.id.end)
                    currentState = 1
                }
                1 -> {
                    _binding?.constraintLayout?.setTransition(R.id., R.id.end)
                    currentState = 2
                }
                2 -> {
                    _binding?.constraintLayout?.setTransition(R.id.end, R.id.start)
                    currentState = 0
                }
            }
            _binding?.constraintLayout?.transitionToEnd()

            // Post a delayed action to run this runnable again after 3 seconds
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the view model
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe changes to the 'text' LiveData in the ViewModel
        homeViewModel.text.observe(viewLifecycleOwner) {
            // Update your UI here
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Start the slide transitions after 3 seconds
        handler.postDelayed(slideRunnable, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove any pending posts of Runnable r that are in the message queue
        handler.removeCallbacks(slideRunnable)
        // Nullify the binding when the view is destroyed
        _binding = null
    }
}