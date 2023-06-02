package com.maxisud.scancare.ui.scanning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.fragment.app.viewModels
import com.maxisud.scancare.R
import com.maxisud.scancare.databinding.FragmentScanningBinding

class ScanningFragment : Fragment() {
    private var _binding: FragmentScanningBinding? = null
    private val binding get() = _binding!!
    private val scanningViewModel by viewModels<ScanningViewModel> {
        ScanningViewModelFactory()
    }
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanningBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {

    }
}