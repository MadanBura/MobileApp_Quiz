package com.ex.quizapplication.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ex.quizapplication.R
import com.ex.quizapplication.databinding.FragmentLiveQuizDetailBinding
import com.ex.quizapplication.model.Quizze

class LiveQuizDetailFragment : Fragment() {

    private lateinit var binding : FragmentLiveQuizDetailBinding
    private var quiz : Quizze? = null
    private val args: LiveQuizDetailFragmentArgs by navArgs()
    private var topicId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        quizId = arguments?.getInt("quizId") ?: -1
        quiz = args.quiz
        topicId = args.topicId

        Log.d("topicId", topicId.toString())
        Log.d("quizObj", topicId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLiveQuizDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<View>(R.id.bottomNavigationView)?.visibility = View.GONE

        binding.tvQuizTitle.text = quiz?.title
        binding.tvQuestionCount.text = "${quiz?.totalQues.toString()} Questions"
        binding.tvQuizDuration.text = quiz?.time

        binding.startquiz.setOnClickListener {
             val action= LiveQuizDetailFragmentDirections
                    .actionLiveQuizDetailToStartQuizFragment(quiz, topicId = topicId)
               Log.e("QuizObj", quiz.toString())
                findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<View>(R.id.bottomNavigationView)?.visibility = View.VISIBLE

    }

}