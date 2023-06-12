package com.maxisud.scancare.ui.result

import android.content.ContentValues
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxisud.scancare.data.response.DiseaseDetailResponse
import com.maxisud.scancare.data.response.PredictionResponseItem
import com.maxisud.scancare.data.response.ProductResponseItem
import com.maxisud.scancare.databinding.ActivityResultBinding
import com.maxisud.scancare.databinding.FragmentResultDetailBinding
import com.maxisud.scancare.ui.SharedRepository
import com.maxisud.scancare.ui.home.HomeViewModel
import com.maxisud.scancare.ui.home.ProductAdapter
import com.maxisud.scancare.ui.product_detail.ProductDetailActivity
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
            val prediction1 = predictions?.get(0)
            val prediction2 = predictions?.get(1)
            val prediction3 = predictions?.get(2)

            diseaseId = prediction1?.id
            binding.squareProb1.setBackgroundColor(Color.parseColor(prediction1?.bgColor ?: "#000000"))
            binding.prob1.text = prediction1?.name
            binding.prob1Percent.text = prediction1?.percentage
            binding.titleResult.text = prediction1?.name
            binding.probabilityHighest.text = prediction1?.percentage
            binding.probabilityContainer.setCardBackgroundColor(Color.parseColor(prediction1?.bgColor ?: "#000000"))

            binding.squareProb2.setBackgroundColor(Color.parseColor(prediction2?.bgColor ?: "#000000"))
            binding.prob2.text = prediction2?.name
            binding.prob2Percent.text = prediction2?.percentage

            binding.squareProb3.setBackgroundColor(Color.parseColor(prediction3?.bgColor ?: "#000000"))
            binding.prob3.text = prediction3?.name
            binding.prob3Percent.text = prediction3?.percentage

            if (diseaseId != null) {
                resultDetailViewModel.getDetailDisease(diseaseId!!)
                resultDetailViewModel.findProductsDisease(diseaseId!!)
            }

            val layoutManager = LinearLayoutManager(context)
            binding.rvRecommended.layoutManager = layoutManager
        })

        resultDetailViewModel.detailDisease.observe(viewLifecycleOwner, Observer { detailDisease ->
            setDetailData(detailDisease)
        })
        resultDetailViewModel.products.observe(viewLifecycleOwner, Observer{ products ->
            setProductsData(products)
        })
        SharedRepository.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setContentVisibility(isLoading?.let { !it } ?: false)
        }
        setContentVisibility(false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        SharedRepository.clear()
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
        binding.titleProductRecommendation.visibility = visibility
    }

    private fun setProductsData(products: List<ProductResponseItem>) {
        val adapter = ProductAdapter(products)
        Log.d(ContentValues.TAG, "products count in Activity: ${products.size}")
        binding.rvRecommended.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback{
            override fun onItemClicked(product: ProductResponseItem){
                val intentToDetail = Intent(requireContext(), ProductDetailActivity::class.java)
                intentToDetail.putExtra("DATA", product.id)
                startActivity(intentToDetail)
            }
        })
        binding.rvRecommended.adapter = adapter
    }

    companion object {
        fun newInstance() = ResultDetailFragment()
        private const val TAG = "ResultDetailFragment"
    }
}