package com.ex.quizapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailsResponse(
    val categories: List<Category>,
    val previousResults: List<PreviousResult>,
    val quizzes: List<Quizze>,
    val userProfile: UserProfile
) : Parcelable