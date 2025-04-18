package com.ex.quizapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PreviousResult(
    val date: String,
    val quizName:String,
    val id: Int,
    val quizId: Int,
    val score: Int,
    val totalQuestions: Int,
    val userId: Int
) : Parcelable