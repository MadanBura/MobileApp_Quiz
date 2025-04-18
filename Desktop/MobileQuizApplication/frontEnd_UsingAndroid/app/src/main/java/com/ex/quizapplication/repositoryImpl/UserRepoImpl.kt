package com.ex.quizapplication.repositoryImpl

import com.ex.quizapplication.datasource.remote.QuizApiService
import com.ex.quizapplication.model.LoginRequest
import com.ex.quizapplication.model.LoginResponse
import com.ex.quizapplication.model.QuizQuestionItemByTopic
import com.ex.quizapplication.model.QuizQuestionsItem
import com.ex.quizapplication.model.QuizResultResponse
import com.ex.quizapplication.model.RegisterRequest
import com.ex.quizapplication.model.RegisterResponse
import com.ex.quizapplication.model.TopicResponse
import com.ex.quizapplication.model.TopicResponseItem
import com.ex.quizapplication.model.UserDetailsResponse
import com.ex.quizapplication.model.UserQuizResponse
import com.ex.quizapplication.repository.UserRepository
import com.ex.quizapplication.utils.TokenManager
import retrofit2.Response
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val quizApiService: QuizApiService,
    private val tokenManager: TokenManager
) : UserRepository {

    override suspend fun signUp(registerRequest: RegisterRequest) :Response<RegisterResponse> {
        val response = quizApiService.registerUser(registerRequest)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        val response = quizApiService.login(loginRequest)
        return if (response.isSuccessful) {
            response.body()?.let { tokenResponse ->
                tokenManager.saveTokens(tokenResponse.token)
                Response.success(tokenResponse)
            } ?: Response.error(response.code(), response.errorBody()!!)
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun goToDashBoard(): Response<UserDetailsResponse> {
        val response = quizApiService.goToDashboard()
        return if (response.isSuccessful) {
            tokenManager.saveUserId(response.body()?.userProfile?.id ?: 0)
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }


    override suspend fun getTopicsByCategoryId(id: Int): Response<List<TopicResponseItem>> {
        val response = quizApiService.getTopicsByCategory(id)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }


    override suspend fun startQuizByQuizId(id: Int): Response<List<QuizQuestionsItem>> {
        val response = quizApiService.getQuizById(id)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }


    override suspend fun submitQuiz(id: Int, list:List<UserQuizResponse>): Response<QuizResultResponse> {
        val response = quizApiService.submitQuiz(quizId = id, list)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }


    override suspend fun startQuizByTopicId(topiccId: Int): Response<List<QuizQuestionItemByTopic>> {
        val response = quizApiService.getQuizByTopic(topiccId)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}