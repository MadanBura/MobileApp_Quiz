package com.ex.quizapplication.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(

    @SerializedName("fullName")
    val fullName : String,
    @SerializedName("email")
    val emailId : String,
    @SerializedName("password")
    val password : String
)
