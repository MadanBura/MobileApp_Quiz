package com.ex.quizapplication.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ex.quizapplication.R
import com.ex.quizapplication.databinding.FragmentResultBinding
import com.ex.quizapplication.model.QuizResultResponse
import com.ex.quizapplication.view.activities.DashBoardActivity

class ResultFragment : Fragment() {

    private lateinit var binding : FragmentResultBinding
    private var resultRes : QuizResultResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultRes = arguments?.getParcelable("resultObj") as? QuizResultResponse
        Log.e("RESULTDATA", resultRes.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.quizTitle.text = resultRes?.quizName
        binding.tvScore.text = resultRes?.score.toString()

        binding.okaybtn.setOnClickListener {
            findNavController().popBackStack(R.id.dashboard_fragment, false)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<View>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
    }
}