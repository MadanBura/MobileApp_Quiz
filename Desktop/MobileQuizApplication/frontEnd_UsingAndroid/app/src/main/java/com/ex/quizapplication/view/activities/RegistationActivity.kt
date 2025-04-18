package com.ex.quizapplication.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.ex.quizapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registation)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()// ensures that all activities in the task are finished, exiting the app completely.
        moveTaskToBack(true) // Moves the app to the background
    }
}