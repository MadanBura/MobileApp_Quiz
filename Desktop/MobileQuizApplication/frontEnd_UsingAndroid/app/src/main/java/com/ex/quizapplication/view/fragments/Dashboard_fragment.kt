package com.ex.quizapplication.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.quizapplication.R
import com.ex.quizapplication.databinding.FragmentDashboardFragmentBinding
import com.ex.quizapplication.model.UserDetailsResponse
import com.ex.quizapplication.utils.ResultState
import com.ex.quizapplication.view.adapters.CategoryAdapters
import com.ex.quizapplication.view.adapters.DashboardQuizAdapters
import com.ex.quizapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Dashboard_fragment : Fragment() {

    private lateinit var binding: FragmentDashboardFragmentBinding
    private var userData: UserDetailsResponse? = null
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Prevent navigating back to login screen
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().moveTaskToBack(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUserProfile()
    }

    override fun onResume() {
        super.onResume()
        // Always fetch the latest profile data
        userViewModel.fetchUpdatedProfile()
    }

    private fun observeUserProfile() {
        userViewModel.profileRes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    Log.d("DashboardFragment", "Loading profile...")
                    binding.prgBar.visibility  = View.VISIBLE
                    binding.dataLayout.visibility = View.GONE
                }

                is ResultState.Success -> {
                    binding.prgBar.visibility  = View.GONE
                    binding.dataLayout.visibility = View.VISIBLE

                    userData = result.data
                    updateUI(userData)
                }

                is ResultState.Error -> {
                    binding.prgBar.visibility  = View.GONE
                    binding.dataLayout.visibility = View.VISIBLE

                    Log.e("DashboardFragment", "Error: ${result.exception.message}")
                    // Show a toast or error UI if needed
                }
            }
        }
    }

    private fun updateUI(userData: UserDetailsResponse?) {
        if (userData == null) return

        binding.tvUserName.text = userData.userProfile.fullname
        binding.tvUserEmail.text = userData.userProfile.email

        // Setup Categories
        val categories = userData.categories ?: emptyList()
        if (categories.isNotEmpty()) {
            val categoryAdapter = CategoryAdapters(categories) { category ->
                val bundle = Bundle().apply {
                    putParcelable("categoryObj", category)
                }
                findNavController().navigate(
                    R.id.action_dashboard_fragment_to_topicFragment,
                    bundle
                )
            }

            binding.categoryRCv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.categoryRCv.adapter = categoryAdapter
            binding.categoryRCv.setHasFixedSize(true)
        } else {
            Log.e("DashboardFragment", "No categories found.")
        }

        // Setup Quizzes
        val quizzes = userData.quizzes ?: emptyList()
        if (quizzes.isNotEmpty()) {
            val quizAdapter = DashboardQuizAdapters(quizzes) { quiz ->
                val action = Dashboard_fragmentDirections.actionDashboardFragmentToLiveQuizDetail(quiz)
                findNavController().navigate(action)
            }

            binding.quizRCV.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.quizRCV.adapter = quizAdapter
            binding.quizRCV.setHasFixedSize(true)
        } else {
            Log.e("DashboardFragment", "No quizzes found.")
        }
    }
}
