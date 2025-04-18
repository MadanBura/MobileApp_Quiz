package com.ex.quizapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize

@Parcelize
data class Quizze(
    @SerializedName("quizId")
    val id: Int,
    val title: String,
    val totalQues : Int,
    val time :String
): Parcelable