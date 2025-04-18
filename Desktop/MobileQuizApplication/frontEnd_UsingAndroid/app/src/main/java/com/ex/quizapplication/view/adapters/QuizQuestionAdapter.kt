package com.ex.quizapplication.view.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ex.quizapplication.model.QuizQuestionsItem
import com.ex.quizapplication.view.fragments.QuestionFragment

class QuizQuestionAdapter(
    fragment: Fragment,
    private val questions: List<QuizQuestionsItem>,
    private val onAnswerSelected: (index: Int, selectedOption: String) -> Unit
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = questions.size

    override fun createFragment(position: Int): Fragment {
        return QuestionFragment.newInstance(
            questions[position],
            position,
            questions.size,
            onAnswerSelected
        )
    }
}