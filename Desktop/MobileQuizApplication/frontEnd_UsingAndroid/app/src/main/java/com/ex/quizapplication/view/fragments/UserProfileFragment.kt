package com.ex.quizapplication.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.quizapplication.R
import com.ex.quizapplication.databinding.FragmentTotalResultBinding
import com.ex.quizapplication.databinding.FragmentUserProfileBinding
import com.ex.quizapplication.model.UserDetailsResponse
import com.ex.quizapplication.utils.ResultState
import com.ex.quizapplication.view.adapters.CategoryAdapters
import com.ex.quizapplication.view.adapters.DashboardQuizAdapters
import com.ex.quizapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding
    private val userViewModel: UserViewModel by viewModels()
    private var userData: UserDetailsResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        userViewModel.fetchUpdatedProfile()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserProfile()


//        binding.logout.setOnClickListener {
//            //userViewModel.logout()
//        }

    }


    private fun observeUserProfile() {
        userViewModel.profileRes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    Log.d("DashboardFragment", "Loading profile...")
                    binding.pgBar.visibility  = View.VISIBLE
                    binding.userLayout.visibility = View.GONE
                }

                is ResultState.Success -> {
                    binding.pgBar.visibility  = View.GONE
                    binding.userLayout.visibility = View.VISIBLE

                    userData = result.data
                    updateUI(userData)
                }

                is ResultState.Error -> {
                    binding.pgBar.visibility  = View.GONE
                    binding.userLayout.visibility = View.VISIBLE

                    Log.e("UserFragment", "Error: ${result.exception.message}")
                }
            }
        }
    }

    private fun updateUI(userData: UserDetailsResponse?) {
        binding.userName.text = userData?.userProfile?.fullname
        binding.userEmail.text = userData?.userProfile?.email
    }

}