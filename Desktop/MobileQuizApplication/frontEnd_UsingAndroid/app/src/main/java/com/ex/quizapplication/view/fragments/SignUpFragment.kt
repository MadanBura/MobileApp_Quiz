package com.ex.quizapplication.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ex.quizapplication.R
import com.ex.quizapplication.databinding.FragmentSignUpBinding
import com.ex.quizapplication.model.RegisterRequest
import com.ex.quizapplication.utils.ResultState
import com.ex.quizapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe LiveData once in onViewCreated
        observeRegistrationResponse()

        binding.btnSignUp.setOnClickListener {
            val name = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPass = binding.etConfirmPassword.text.toString().trim()

            if (validateAllFields(name, email, password, confirmPass)) {
                registerUser(name, email, password)
            }
        }

        // Navigate to Sign In page
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        userViewModel.registerUser(RegisterRequest(name, email, password))
    }

    private fun observeRegistrationResponse() {
        userViewModel.registrationRes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Success -> {
                    showToast("Registration successful! Please log in.")
                    findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                }

                is ResultState.Error -> {
                    showToast(result.exception.message ?: "Registration failed")
                }

                else -> {}
            }
        }
    }

    private fun validateAllFields(
        name: String,
        email: String,
        password: String,
        confirmPass: String
    ): Boolean {
        return when {
            name.isBlank() -> {
                showToast("Name cannot be empty")
                false
            }

            email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Invalid email address")
                false
            }

            password.length < 6 -> {
                showToast("Password must be at least 6 characters long")
                false
            }

            password != confirmPass -> {
                showToast("Passwords do not match")
                false
            }

            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
