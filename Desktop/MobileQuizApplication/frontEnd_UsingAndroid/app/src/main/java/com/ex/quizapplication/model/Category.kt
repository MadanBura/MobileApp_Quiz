package com.ex.quizapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id : Int,
    val categoryName:String
): Parcelable