package com.ex.quizapplication.repository

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
import retrofit2.Response

interface UserRepository {

    suspend fun signUp(registerRequest : RegisterRequest) : Response<RegisterResponse>
    suspend fun login(loginRequest: LoginRequest) : Response<LoginResponse>
    suspend fun goToDashBoard() : Response<UserDetailsResponse>

    suspend fun getTopicsByCategoryId(id:Int) : Response<List<TopicResponseItem>>
    suspend fun startQuizByQuizId(id:Int) : Response<List<QuizQuestionsItem>>

    suspend fun startQuizByTopicId(topiccId:Int) : Response<List<QuizQuestionItemByTopic>>
    suspend fun submitQuiz(id:Int, list:List<UserQuizResponse>) : Response<QuizResultResponse>

}