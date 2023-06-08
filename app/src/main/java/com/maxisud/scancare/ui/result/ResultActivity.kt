package com.maxisud.scancare.ui.result

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.maxisud.scancare.R
import com.maxisud.scancare.databinding.ActivityResultBinding
import com.maxisud.scancare.ui.SharedRepository
import com.maxisud.scancare.ui.scanning.ScanningViewModel
import com.maxisud.scancare.ui.scanning.ScanningViewModelFactory

class ResultActivity : AppCompatActivity() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var _binding : ActivityResultBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        // Add the callback
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset < 0) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val bottomSheetFragment = ResultDetailFragment.newInstance()
        fragmentTransaction.replace(R.id.bottom_sheet_fragment_container, bottomSheetFragment)
        fragmentTransaction.commit()

        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        Glide.with(this)
            .load(imageUri)
            .into(binding.photoResult)

        SharedRepository.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}