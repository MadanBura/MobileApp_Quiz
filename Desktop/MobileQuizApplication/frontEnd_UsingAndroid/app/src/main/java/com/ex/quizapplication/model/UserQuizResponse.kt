package com.ex.quizapplication.model

import com.google.gson.annotations.SerializedName

data class UserQuizResponse(
    val questionId: Int,
    val userId:Int,
    @SerializedName("userAnswer")
    val selectedAnswer: String
)