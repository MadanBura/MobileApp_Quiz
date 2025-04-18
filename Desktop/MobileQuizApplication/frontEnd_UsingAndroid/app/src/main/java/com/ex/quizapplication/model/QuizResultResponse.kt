package com.ex.quizapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizResultResponse(
    val date: String,
    val quizId: Int,
    val quizName:String?,
    val score: Int,
    val totalQuestions: Int
):Parcelable