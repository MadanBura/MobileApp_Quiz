package com.ex.quizapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ex.quizapplication.model.LoginRequest
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
import com.ex.quizapplication.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registrationRes = MutableLiveData<ResultState<RegisterResponse>>()
    val registrationRes: LiveData<ResultState<RegisterResponse>> get() = _registrationRes

    private val _profileRes = MutableLiveData<ResultState<UserDetailsResponse>>()
    val profileRes : LiveData<ResultState<UserDetailsResponse>> get() = _profileRes

    private val _topics = MutableLiveData<List<TopicResponseItem>>()
    val topics: LiveData<List<TopicResponseItem>> = _topics

    private val _quizQuesRes = MutableLiveData<List<QuizQuestionsItem>>()
    val quizQuesRes : LiveData<List<QuizQuestionsItem>> = _quizQuesRes

    private val _quizQuesResTopic = MutableLiveData<List<QuizQuestionItemByTopic>>()
    val quizQuesResTopic : LiveData<List<QuizQuestionItemByTopic>> = _quizQuesResTopic

    private val _resultLiveData = MutableLiveData<QuizResultResponse>()
    val resultLiveData : LiveData<QuizResultResponse> = _resultLiveData




    fun registerUser(userRequest: RegisterRequest) {
        _registrationRes.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val response = userRepository.signUp(userRequest)

                if (response.isSuccessful && response.body() != null) {
                    _registrationRes.value = ResultState.Success(response.body()!!)
                } else {
                    _registrationRes.value =
                        ResultState.Error(Throwable("Error: ${response.message()}"))
                }
            } catch (e: Exception) {
                _registrationRes.value = ResultState.Error(e)
            }
        }
    }

    fun login(loginRequest: LoginRequest){
            _profileRes.value = ResultState.Loading

        viewModelScope.launch {
            val response = userRepository.login(loginRequest)
            if(response.isSuccessful){
                val accessToken = response.body()?.token
                if(!accessToken.isNullOrEmpty()){
                    val profileRes = userRepository.goToDashBoard()
                    _profileRes.value = if (profileRes.isSuccessful) {
                        ResultState.Success(profileRes.body()!!)
                    } else {
                        ResultState.Error(Exception("Failed to fetch profile: ${profileRes.message()}"))
                    }

                }else {
                    _profileRes.value = ResultState.Error(Exception("Access token is null"))
                }
            }else {
                _profileRes.value = ResultState.Error(Exception("Login failed: ${response.message()}"))
            }

        }

    }

    fun getTopicsByCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                val response = userRepository.getTopicsByCategoryId(categoryId)
                if (response.isSuccessful && response.body() != null) {
                    _topics.postValue(response.body())
                } else {
                    Log.e("UserViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception in getTopicsByCategory", e)
            }
        }
    }

    fun startQuizByQuizId(quizId:Int){
        viewModelScope.launch {
            try {
                val response = userRepository.startQuizByQuizId(quizId)
                if(response.isSuccessful && response.body()!=null){
                    _quizQuesRes.value = response.body()
                }else{
                    Log.e("UserViewModel", "Error: ${response.message()}")
                }
            }catch (e : Exception){
                Log.e("UserViewModel", "Exception in getTopicsByCategory", e)
            }
        }
    }

    fun startQuizByTopicId(topicId:Int){
        viewModelScope.launch {
            try {
                val response = userRepository.startQuizByTopicId(topicId)
                Log.e("topicRes", response.body().toString())
                if(response.isSuccessful && response.body()!=null){
                    _quizQuesResTopic.value = response.body()
                }else{
                    Log.e("UserViewModel", "Error: ${response.message()}")
                }
            }catch (e : Exception){
                Log.e("UserViewModel", "Exception in getTopicsByCategory", e)
            }
        }
    }

    fun submitQuiz(quizId: Int, responses: List<UserQuizResponse>) {
        viewModelScope.launch {
            try {
                val result = userRepository.submitQuiz(quizId, responses)
                _resultLiveData.value = result.body()
            } catch (e: Exception) {
                Log.e("Quiz", "Error submitting quiz", e)
            }
        }
    }

    fun fetchUpdatedProfile() {
        _profileRes.value = ResultState.Loading

        viewModelScope.launch {
            try {
                val profileResponse = userRepository.goToDashBoard()
                _profileRes.value = if (profileResponse.isSuccessful) {
                    ResultState.Success(profileResponse.body()!!)
                } else {
                    ResultState.Error(Exception("Failed to fetch profile: ${profileResponse.message()}"))
                }
            } catch (e: Exception) {
                _profileRes.value = ResultState.Error(e)
            }
        }
    }




}