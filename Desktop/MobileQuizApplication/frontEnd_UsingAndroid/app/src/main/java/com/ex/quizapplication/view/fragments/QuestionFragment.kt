package com.ex.quizapplication.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ex.quizapplication.R
import com.ex.quizapplication.databinding.FragmentQuestionBinding
import com.ex.quizapplication.model.QuizQuestionsItem
import com.ex.quizapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionFragment : Fragment() {

    private lateinit var binding: FragmentQuestionBinding
    private var onAnswerSelected: ((Int, String) -> Unit)? = null
//    private var index: Int = 0
//    private var totalQuestion : Int = 0

    companion object {
        fun newInstance(
            question: QuizQuestionsItem,
            index: Int,
            total: Int,
            onAnswerSelected: (index: Int, selectedOption: String) -> Unit
        ): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle().apply {
                putParcelable("question", question)
                putInt("index", index)
                putInt("total", total)
                putBoolean("isLast", index == total - 1)
            }
            fragment.arguments = args
            fragment.onAnswerSelected = onAnswerSelected
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<View>(R.id.bottomNavigationView)?.visibility = View.GONE

        val question = arguments?.getParcelable<QuizQuestionsItem>("question")
       val index = arguments?.getInt("index") ?: 0
       val totalQuestion = arguments?.getInt("total") ?: 0
        val isLastQuestion = arguments?.getBoolean("isLast") ?: false

        val radioGroup = binding.optionGroup
        val btnContinue = binding.btnContinue

        question?.let {
            binding.questionText.text = it.question_title
            binding.radioOptionA.text = it.option1
            binding.radioOptionB.text = it.option2
            binding.radioOptionC.text = it.option3
            binding.radioOptionD.text = it.option4
        }

        binding.btnContinue.text = if (isLastQuestion) "Finish" else "Continue"

        // Initially disable continue button
        btnContinue.isEnabled = false
        btnContinue.setBackgroundColor(resources.getColor(R.color.gray))

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            btnContinue.isEnabled = true
            btnContinue.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            // Reset all radio buttons
            for (i in 0 until radioGroup.childCount) {
                val rb = radioGroup.getChildAt(i) as? RadioButton
                rb?.apply {
//                        isChecked = id == this.id // keep logic selection valid
                    setBackgroundResource(R.drawable.bg_radio_default)
                    setTextColor(resources.getColor(R.color.black))
                }
            }

            // Set selected button styling
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            selectedRadioButton.setBackgroundResource(R.drawable.bg_radio_selected)
            selectedRadioButton.setTextColor(resources.getColor(R.color.white))

            val selectedText = selectedRadioButton.text.toString()

            binding.btnContinue.setOnClickListener {
                if (isLastQuestion) {
                    showFinishConfirmationDialog(selectedText, index)
                } else {
                    onAnswerSelected?.invoke(index, selectedText)
                }
            }
        }
        updateStepCount(index + 1, totalQuestion) // Update with dynamic total if needed
    }

    fun updateStepCount(current: Int, total: Int) {
        binding.progressBar.progress = ((current.toFloat() / total) * 100).toInt()
    }



    private fun showFinishConfirmationDialog(selectedText: String, index: Int) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Finish Quiz")
            .setMessage("Are you sure you want to finish the quiz?")
            .setPositiveButton("Yes") { dialog, _ ->
                onAnswerSelected?.invoke(index, selectedText)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
}