package com.ex.quizapplication.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ex.quizapplication.R
import com.ex.quizapplication.model.UserDetailsResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewDashBoard) as NavHostFragment
        navController = navHostFragment.navController

        // 🔗 Connect BottomNav with NavController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)

        // Receive and pass userData only if coming for the first time
        val userData = intent.getParcelableExtra("data") as? UserDetailsResponse
        if (savedInstanceState == null && userData != null) {
            val bundle = Bundle().apply {
                putParcelable("userData", userData)
            }
            navController.navigate(R.id.dashboard_fragment, bundle)
        }


        bottomNav.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val bundle = Bundle().apply {
                        putParcelable("userData", userData)
                    }
                    navController.navigate(R.id.dashboard_fragment, bundle)
                    true
                }
                R.id.nav_graph -> {

                    val previousResult = userData?.previousResults
                    val bundle = Bundle().apply {
                        putParcelableArrayList("previousResult", ArrayList(previousResult))
                    }

                    navController.navigate(R.id.totalResultFragment, bundle)
                    true
                }

                R.id.nav_user -> {
                    val bundle = Bundle().apply {
                        putParcelable("userData", userData)
                    }
                    navController.navigate(R.id.userProfileFragment, bundle)
                    true
                }

                else -> false
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        finishAffinity()
//        moveTaskToBack(true)
    }


}