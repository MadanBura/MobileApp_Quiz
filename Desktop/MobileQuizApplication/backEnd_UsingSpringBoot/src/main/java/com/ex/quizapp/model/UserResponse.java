package com.ex.quizapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserResponse {

	private Integer questionId;
	private Integer userId;
	private String userAnswer;

}
