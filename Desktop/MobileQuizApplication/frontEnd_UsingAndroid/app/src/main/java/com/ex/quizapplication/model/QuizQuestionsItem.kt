package com.ex.quizapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class QuizQuestionsItem(
    val category: String,
    val id: Int,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val question_title: String
) : Parcelable