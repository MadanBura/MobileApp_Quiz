package com.ex.quizapp.dto_classes;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;



@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class QuizDtoResponse {
	
	private Integer quizId;
	private String title;
	private Integer totalQues;
	private String time;
}
