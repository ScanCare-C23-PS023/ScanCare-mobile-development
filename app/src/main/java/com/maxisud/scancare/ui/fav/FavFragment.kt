package com.maxisud.scancare.ui.fav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.maxisud.scancare.ui.Login.LoginActivity
import com.maxisud.scancare.databinding.FragmentFavBinding
import com.maxisud.scancare.ui.dashboard.FavViewModel

class FavFragment : Fragment() {

    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favViewModel =
            ViewModelProvider(this).get(FavViewModel::class.java)

        _binding = FragmentFavBinding.inflate(inflater, container, false)
        val root: View = binding.root

        favoritesAdapter = FavoritesAdapter(emptyList(), requireContext())
        binding.rvFavorites.adapter = favoritesAdapter

        favViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            favoritesAdapter.items = favorites
            favoritesAdapter.notifyDataSetChanged()

            binding.progressBar.visibility = View.GONE
        }

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            // Show progress bar
            binding.progressBar.visibility = View.VISIBLE

            favViewModel.fetchFavorites(currentUserId)
            binding.rvFavorites.visibility = View.VISIBLE
            binding.tvNotLoggedIn.visibility = View.GONE
            binding.btnLogin.visibility = View.GONE
        } else {
            // Hide progress bar
            binding.progressBar.visibility = View.GONE

            binding.rvFavorites.visibility = View.GONE
            binding.tvNotLoggedIn.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.VISIBLE
            binding.btnLogin.setOnClickListener {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}