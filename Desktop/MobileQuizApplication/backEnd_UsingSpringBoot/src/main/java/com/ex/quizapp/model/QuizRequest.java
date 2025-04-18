package com.ex.quizapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuizRequest {
	  private Integer categoryid;
	    private int no;
	    private String title;
	    private String time;
}
