package com.ex.quizapplication.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ex.quizapplication.R
import com.ex.quizapplication.databinding.FragmentSignInBinding
import com.ex.quizapplication.model.LoginRequest
import com.ex.quizapplication.model.UserDetailsResponse
import com.ex.quizapplication.utils.ResultState
import com.ex.quizapplication.view.activities.DashBoardActivity
import com.ex.quizapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().moveTaskToBack(true)
        }

        binding.btnLogin.setOnClickListener {
//            val email = binding.etEmail.text.toString().trim()
//            val password = binding.etPassword.text.toString().trim()

            val email = "demo@test.com"
            val password = "madan123"


            if (validateFields(email, password)) {
                loginUser(email, password)
            }
        }

        // Navigate to Sign Up Page
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun loginUser(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.visibility = View.GONE

        val loginRequest = LoginRequest(email = email, password= password)
        userViewModel.login(loginRequest)

        userViewModel.profileRes.observe(viewLifecycleOwner) { result ->
            when (result) {

                is ResultState.Loading -> {
                    binding.btnLogin.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.visibility = View.GONE

                    Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()
                    launchDashBoard(result.data)
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Login Failed: ${result.exception.message}", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    private fun validateFields(email: String, password: String): Boolean {
        return when {
            email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Invalid email address")
                false
            }
            password.length < 6 -> {
                showToast("Password must be at least 6 characters long")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun launchDashBoard(data : UserDetailsResponse){

//        val userDetails = UserDetailsResponse(data.categories, data.previousResults, data.quizzes, data.userProfile)
//        Intent(requireContext(), DashBoardActivity::class.java).also {
//            it.putExtra("data", userDetails)
//            startActivity(it)
//        }

        if (data!= null) {
            Intent(requireContext(), DashBoardActivity::class.java).also {
                it.putExtra("data", data)
                Log.e("SignInFragment", "✅ Passing userDetails: $data")
                startActivity(it)
            }
        } else {
            Log.e("SignInFragment", "❌ userDetails is NULL before passing to DashBoardActivity")
        }
    }

}
