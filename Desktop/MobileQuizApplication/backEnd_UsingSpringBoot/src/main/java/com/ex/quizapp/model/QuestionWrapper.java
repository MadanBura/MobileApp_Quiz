package com.ex.quizapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class QuestionWrapper {
	
	
	@Id
	@GeneratedValue()
	private Integer id;
	private String category;
	private String question_title;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	
}
