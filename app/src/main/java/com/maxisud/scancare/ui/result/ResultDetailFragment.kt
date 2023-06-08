package com.maxisud.scancare.ui.result

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.maxisud.scancare.data.response.DiseaseDetailResponse
import com.maxisud.scancare.databinding.ActivityResultBinding
import com.maxisud.scancare.databinding.FragmentResultDetailBinding
import com.maxisud.scancare.ui.SharedRepository
import com.maxisud.scancare.ui.home.HomeViewModel
import com.maxisud.scancare.ui.scanning.ScanningViewModel
import com.maxisud.scancare.ui.scanning.ScanningViewModelFactory

class ResultDetailFragment : Fragment() {

    private var _binding: FragmentResultDetailBinding? = null
    private val binding get() = _binding!!


    private val resultDetailViewModel by viewModels<ResultDetailViewModel>{
        ResultDetailViewModelFactory()
    }
    private var diseaseId : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultDetailBinding.inflate(layoutInflater)
        SharedRepository.prediction.observe(viewLifecycleOwner, Observer { predictions ->
            val prediction1 = predictions[0]
            val prediction2 = predictions[1]
            val prediction3 = predictions[2]

            diseaseId = prediction1.id
            binding.squareProb1.setBackgroundColor(Color.parseColor(prediction1.bgColor))
            binding.prob1.text = prediction1.name
            binding.prob1Percent.text = prediction1.percentage
            binding.titleResult.text = prediction1.name
            binding.probabilityHighest.text = prediction1.percentage
            binding.probabilityContainer.setCardBackgroundColor(Color.parseColor(prediction1.bgColor))

            binding.squareProb2.setBackgroundColor(Color.parseColor(prediction2.bgColor))
            binding.prob2.text = prediction2.name
            binding.prob2Percent.text = prediction2.percentage

            binding.squareProb3.setBackgroundColor(Color.parseColor(prediction3.bgColor))
            binding.prob3.text = prediction3.name
            binding.prob3Percent.text = prediction3.percentage

            if (diseaseId != null) {
                resultDetailViewModel.getDetailDisease(diseaseId!!)
            }


        })

        resultDetailViewModel.detailDisease.observe(viewLifecycleOwner, Observer { detailDisease ->
            setDetailData(detailDisease)
        })
        SharedRepository.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setContentVisibility(!isLoading)
        }
        setContentVisibility(false)
        return binding.root
    }

    private fun setDetailData(disease: DiseaseDetailResponse){
        binding.tvDetailDisease.text = disease.characteristics
    }


    private fun setContentVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.subTitleResult.visibility = visibility
        binding.titleResult.visibility = visibility
        binding.probabilityContainer.visibility = visibility
        binding.titlePossibility.visibility = visibility
        binding.squareProb1.visibility = visibility
        binding.prob1.visibility = visibility
        binding.prob1Percent.visibility = visibility
        binding.squareProb2.visibility = visibility
        binding.prob2.visibility = visibility
        binding.prob2Percent.visibility = visibility
        binding.squareProb3.visibility = visibility
        binding.prob3.visibility = visibility
        binding.prob3Percent.visibility = visibility
        binding.horizontalLine1.visibility = visibility
        binding.titleDetailDisease.visibility = visibility
        binding.horizontalLine2.visibility = visibility
        binding.tvDetailDisease.visibility = visibility
    }

    companion object {
        fun newInstance() = ResultDetailFragment()
        private const val TAG = "ResultDetailFragment"
    }
}