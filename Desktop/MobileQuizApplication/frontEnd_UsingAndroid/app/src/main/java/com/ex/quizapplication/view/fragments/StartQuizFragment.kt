package com.ex.quizapplication.view.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ex.quizapplication.R
import com.ex.quizapplication.databinding.FragmentStartQuizBinding
import com.ex.quizapplication.model.QuizQuestionItemByTopic
import com.ex.quizapplication.model.QuizQuestionsItem
import com.ex.quizapplication.model.UserQuizResponse
import com.ex.quizapplication.model.Quizze
import com.ex.quizapplication.utils.TokenManager
import com.ex.quizapplication.view.adapters.QuizQuestionAdapter
import com.ex.quizapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class StartQuizFragment : Fragment() {

    private lateinit var binding: FragmentStartQuizBinding
    private lateinit var countDownTimer: CountDownTimer
    private val totalTimeInMillis = 1 * 60 * 500L // 1 minute
    private lateinit var tokenManager: TokenManager

    private val args: StartQuizFragmentArgs by navArgs()
    private var quiz: Quizze? = null
    private var topicId: Int = -1
    private val userViewModel: UserViewModel by viewModels()
    private var currentIndex = 0
    private var totalQuestions = 0
    private lateinit var questionsList: List<QuizQuestionsItem>
    private val selectedAnswers = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quiz = args.quiz
        topicId = args.topicId

        Log.d("topicIdST", topicId.toString())
        Log.d("quizObj", topicId.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<View>(R.id.bottomNavigationView)?.visibility = View.GONE
        tokenManager = TokenManager(requireContext())

        startTimer()

        // Start Quiz based on available input
        if (quiz != null) {
            binding.quizTitle.text = quiz?.title ?: "Quiz"
            userViewModel.startQuizByQuizId(quiz!!.id)
        } else if (topicId != -1) {
            binding.quizTitle.text = "Quiz on Topic $topicId"
            userViewModel.startQuizByTopicId(topicId)
        } else {
            binding.quizTitle.text = "No Quiz Found"
        }

        val liveData =
            if (quiz != null) userViewModel.quizQuesRes else userViewModel.quizQuesResTopic
        liveData.observe(viewLifecycleOwner) { questions ->
            questionsList = questions.mapNotNull {
                when (it) {
                    is QuizQuestionItemByTopic -> {
                        QuizQuestionsItem(
                            category = it.topicName,
                            id = it.id,
                            option1 = it.option1,
                            option2 = it.option2,
                            option3 = it.option3,
                            option4 = it.option4,
                            question_title = it.question_title
                        )
                    }
                    is QuizQuestionsItem -> it
                    else -> null
                }
            }
            Log.d("questionList", "$questions")
            Log.d("questionList", "${questionsList.toString()}")
            totalQuestions = questionsList.size

            val adapter = QuizQuestionAdapter(this, questionsList) { index, selectedOption ->
                selectedAnswers[index] = selectedOption
                goToNextQuestion()
            }

            binding.viewPagerQuestions.adapter = adapter
            binding.viewPagerQuestions.isUserInputEnabled = false
        }

    }


    private fun goToNextQuestion() {
        currentIndex++
        if (currentIndex < totalQuestions) {
            binding.viewPagerQuestions.setCurrentItem(currentIndex, true)
            updateProgress(currentIndex + 1, totalQuestions)
        } else {
            updateProgress(totalQuestions, totalQuestions)
            navigateToResultScreen()
        }
    }

    private fun updateProgress(currentIndex: Int, totalQuestions: Int) {
        val currentFragment = childFragmentManager.findFragmentByTag("f$currentIndex")
        (currentFragment as? QuestionFragment)?.updateStepCount(currentIndex + 1, totalQuestions)
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                binding.countdownTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.countdownTimer.text = "00:00"
                showAlertDialog()
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }

    private fun prepareUserResponses(): List<UserQuizResponse> {
        return questionsList.mapIndexed { index, question ->
            val answer = selectedAnswers[index] ?: "N/A"
            UserQuizResponse(
                questionId = question.id,
                userId = tokenManager.getUserId() ?: 0,
                selectedAnswer = answer
            )
        }
    }

    private fun showAlertDialog() {
        val attempted = selectedAnswers.size
        val skipped = totalQuestions - attempted

        val message = """
            Time's up!
            Total Questions: $totalQuestions
            Attempted: $attempted
            Skipped: $skipped
        """.trimIndent()

        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Quiz Finished")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("View Result") { dialog, _ ->
                dialog.dismiss()
                navigateToResultScreen()
            }
            .create()

        alertDialog.show()
    }

    private fun navigateToResultScreen() {
        val responses = prepareUserResponses()
        if (quiz != null) {
            userViewModel.submitQuiz(quiz!!.id, responses)
        } else {
            userViewModel.submitQuiz(topicId, responses)
        }

        userViewModel.resultLiveData.observe(viewLifecycleOwner) { result ->
            result?.let {
                val bundle = Bundle().apply {
                    putParcelable("resultObj", it)
                }
                if (isAdded && findNavController().currentDestination?.id == R.id.startQuizFragment) {
                    findNavController().navigate(
                        R.id.action_startQuizFragment_to_resultFragment,
                        bundle
                    )
                }
            }
        }
    }
}
