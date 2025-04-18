package com.ex.quizapplication.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("quizAppCred", Context.MODE_PRIVATE)

    fun saveUserId(userId:Int){
        prefs.edit().putInt("USER_ID", userId)
            .apply()
    }

    fun saveTokens(accessToken: String) {
        prefs.edit()
            .putString("ACCESS_TOKEN", accessToken)
            .apply()
    }

    fun getUserId():Int? = prefs.getInt("USER_ID", 0)
    fun getAccessToken(): String? = prefs.getString("ACCESS_TOKEN", null)
    fun getAppFlagDetails(): Boolean = prefs.getBoolean("IS_FIRST_TIME", true)

    fun setFirstTimeFlag(isFirstTime: Boolean) {
        prefs.edit().putBoolean("IS_FIRST_TIME", isFirstTime).apply()
    }

    fun clearTokens() {
        prefs.edit().clear().apply()
    }
}
