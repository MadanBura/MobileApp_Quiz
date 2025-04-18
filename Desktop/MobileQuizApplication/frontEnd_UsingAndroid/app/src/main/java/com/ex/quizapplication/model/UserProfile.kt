package com.ex.quizapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile(
    val email: String,
    val fullname: String,

    @SerializedName("id")
    val id: Int
) : Parcelable