package com.ex.quizapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuizResponse {
	
	private Quiz quiz;
	private String message;
	
}
