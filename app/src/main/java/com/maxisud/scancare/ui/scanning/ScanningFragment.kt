package com.maxisud.scancare.ui.scanning

import android.app.Activity
import com.yalantis.ucrop.UCrop
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.maxisud.scancare.databinding.FragmentScanningBinding
import com.maxisud.scancare.ui.result.ResultActivity
import com.yalantis.ucrop.UCropActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class ScanningFragment : Fragment() {
    private var _binding: FragmentScanningBinding? = null
    private val binding get() = _binding!!
    private val scanningViewModel by activityViewModels<ScanningViewModel> {
        ScanningViewModelFactory()
    }
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var getFile: File? = null
    private var camera: Camera? = null

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireActivity())
                getFile = myFile
                displayConfirm(myFile)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermissionsGranted()) {
            startCamera()
            binding.captureImage.setOnClickListener { takePhoto() }
            binding.gallery.setOnClickListener { startGallery() }
            binding.backButton.setOnClickListener {
                requireActivity().onBackPressed()
            }
            binding.flash.setOnClickListener {
                when(camera?.cameraInfo?.torchState?.value) {
                    TorchState.ON -> {
                        camera?.cameraControl?.enableTorch(false)
                    }
                    TorchState.OFF -> {
                        camera?.cameraControl?.enableTorch(true)
                    }
                }
            }
            binding.switchCamera.setOnClickListener {
                cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA

                startCamera()
            }
            scanningViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCROP_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let { uri ->
                val myFile = uriToFile(uri, requireActivity())
                getFile = myFile

                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), myFile)
                val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("image", myFile.name, requestFile)
                scanningViewModel.uploadImage(multipartBody)
                scanningViewModel.setCroppedImageUri(uri)
                scanningViewModel.croppedImageUri.observe(viewLifecycleOwner, { uri ->
                    val intent = Intent(requireActivity(), ResultActivity::class.java)
                    intent.putExtra("imageUri", uri.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                })
            }
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanningBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }



    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())


        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Failed to start the camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            camera = cameraProvider.bindToLifecycle(
                viewLifecycleOwner, cameraSelector, preview, imageCapture
            )

        }, ContextCompat.getMainExecutor(requireContext()))
    }
    private fun displayConfirm(file: File) {
        val destinationUri = Uri.fromFile(createTempFile(requireContext()))

        val options = UCrop.Options().apply {
            setCompressionQuality(90)
            setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL)
        }

        UCrop.of(Uri.fromFile(file), destinationUri)
            .withOptions(options)
            .start(requireContext(), this, UCROP_REQUEST_CODE)
    }
    private fun displayCroppedConfirm(file: File) {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)

        hideCameraControls()

        binding.viewFinder.visibility = View.GONE
        binding.confirmImageView.visibility = View.VISIBLE
        binding.confirmImageView.setImageBitmap(bitmap)

        binding.checkPhoto.visibility = View.VISIBLE
        binding.cancelPhoto.visibility = View.VISIBLE

        binding.checkPhoto.setOnClickListener { acceptPhoto(file) }
        binding.cancelPhoto.setOnClickListener { cancelPhoto() }
    }



    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(requireContext())

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to take picture.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    lifecycleScope.launch {
                        binding.progressBar.visibility = View.VISIBLE

                        val reducedFile = reduceFileImage(photoFile)
                        rotateFile(reducedFile, cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)

                        val bitmap = BitmapFactory.decodeFile(reducedFile.absolutePath)
                        val viewFinderAspectRatio = binding.viewFinder.width.toFloat() / binding.viewFinder.height.toFloat()
                        val croppedBitmap = cropImageToAspectRatio(bitmap, viewFinderAspectRatio)

                        val croppedFile = createFile(requireContext())
                        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(croppedFile))

                        delay(1000)


                        binding.progressBar.visibility = View.GONE

                        displayConfirm(croppedFile)
                    }
                }
            }
        )
    }



    private fun acceptPhoto(file: File) {
        val intent = Intent().apply {
            putExtra("picture", file)
            putExtra("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        resetCamera()
    }


    private fun cancelPhoto() {
        resetCamera()
    }

    private fun resetCamera() {
        binding.viewFinder.visibility = View.VISIBLE
        binding.confirmImageView.visibility = View.GONE

        showCameraControls()
    }

    private fun hideCameraControls() {
        binding.captureImage.visibility = View.GONE
        binding.gallery.visibility = View.GONE
        binding.switchCamera.visibility = View.GONE
        binding.backButton.visibility = View.GONE
        binding.flash.visibility = View.GONE

        binding.checkPhoto.visibility = View.VISIBLE
        binding.cancelPhoto.visibility = View.VISIBLE
    }

    private fun showCameraControls() {
        binding.captureImage.visibility = View.VISIBLE
        binding.gallery.visibility = View.VISIBLE
        binding.switchCamera.visibility = View.VISIBLE
        binding.backButton.visibility = View.VISIBLE
        binding.flash.visibility = View.VISIBLE

        binding.checkPhoto.visibility = View.GONE
        binding.cancelPhoto.visibility = View.GONE
    }




    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CAMERA_X_RESULT: Int = 200
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val UCROP_REQUEST_CODE = 101

    }
}