package com.maxisud.scancare

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.maxisud.scancare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fab = binding.buttonScan

        val pressedColor = ContextCompat.getColor(this, R.color.transparent)
        val defaultColor = ContextCompat.getColor(this, R.color.black)

        fab.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateBackgroundColor(fab, defaultColor, pressedColor)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    animateBackgroundColor(fab, pressedColor, defaultColor)
                }
            }
            false
        }

        val navView: BottomNavigationView = binding.bottomNavInstructor

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_scanning, R.id.navigation_favorite
            )
        )
        navView.setupWithNavController(navController)
    }

    private fun animateBackgroundColor(view: View, fromColor: Int, toColor: Int) {
        val duration = 200L // Adjust the duration as desired

        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor)
        colorAnimation.duration = duration
        colorAnimation.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            view.setBackgroundColor(color)
        }
        colorAnimation.start()

        val alphaAnimation = ValueAnimator.ofInt(Color.alpha(fromColor), Color.alpha(toColor))
        alphaAnimation.duration = duration
        alphaAnimation.addUpdateListener { animator ->
            val alpha = animator.animatedValue as Int
            view.background.alpha = alpha
        }
        alphaAnimation.start()
    }
}