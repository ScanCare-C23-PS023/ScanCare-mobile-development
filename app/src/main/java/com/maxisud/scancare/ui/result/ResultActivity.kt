package com.maxisud.scancare.ui.result

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.maxisud.scancare.MainActivity
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

        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.isFitToContents = true

        val scanningViewModel by viewModels<ScanningViewModel>()

        scanningViewModel.toastMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        SharedRepository.isLoading.observe(this) { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        SharedRepository.isTimeout.observe(this) { isTimeout ->
            if (isTimeout == true) {
                Toast.makeText(this, "Prediction took too long. Please try again.", Toast.LENGTH_SHORT).show()
                SharedRepository.setTimeout(false)
            }
        }

        binding.guideline1.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val guidelinesLocation = IntArray(2)
                binding.guideline1.getLocationOnScreen(guidelinesLocation)

                val photoResultLocation = IntArray(2)
                binding.photoResult.getLocationOnScreen(photoResultLocation)

                val screenHeight = resources.displayMetrics.heightPixels

                val newPeekHeight = screenHeight - guidelinesLocation[1] + photoResultLocation[1]
                bottomSheetBehavior.peekHeight = newPeekHeight

                binding.guideline1.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })


        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val bottomSheetFragment = ResultDetailFragment.newInstance()
        fragmentTransaction.replace(R.id.bottom_sheet_fragment_container, bottomSheetFragment)
        fragmentTransaction.commit()

        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        Glide.with(this)
            .load(imageUri)
            .into(binding.photoResult)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
    }
}