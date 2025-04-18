package com.ex.quizapplication.datasource.remote

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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface QuizApiService {

    @POST("auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest) : Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body loginRequest : LoginRequest) : Response<LoginResponse>

    @GET("dashboard/getUserDetail")
    suspend fun goToDashboard(
//        @Header("Authorization") token: String
    ) : Response<UserDetailsResponse>

    @GET("quiz/topics/{id}")
    suspend fun getTopicsByCategory(@Path("id") categoryId: Int): Response<List<TopicResponseItem>>

    @GET("quiz/getQuiz/{id}")
    suspend fun getQuizById(@Path("id") quizId: Int) : Response<List<QuizQuestionsItem>>

    @Headers("Content-Type: application/json")
    @POST("quiz/submit/{id}")
    suspend fun submitQuiz(
        @Path("id") quizId: Int,
        @Body responses: List<UserQuizResponse>
    ): Response<QuizResultResponse>

    @GET("quiz/createQuizByTopic/{id}")
    suspend fun getQuizByTopic(@Path("id") quizId: Int) : Response<List<QuizQuestionItemByTopic>>


}