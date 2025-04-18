package com.ex.quizapp.model;

import java.util.List;

import com.ex.quizapp.dto_classes.CategoryResponseDto;
import com.ex.quizapp.dto_classes.QuizDtoResponse;
import com.ex.quizapp.dto_classes.ResultResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDetailResponse {
	
		private UserScreenResponse userProfile;
	    private List<CategoryResponseDto> categories;
	    private List<QuizDtoResponse> quizzes;
	    private List<ResultResponseDto> previousResults;
	
}
