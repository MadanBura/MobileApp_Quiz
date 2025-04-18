package com.ex.quizapp.dto_classes;


import lombok.Data;
import java.util.List;

@Data
public class TopicDto {

	  private Integer id;
	    private String name;
	    private Integer categoryId;
	    private Integer totalQues;
	    private String time;
//	    val totalQues : Int,
//	    val time :String
//	    private List<QuestionDto> questions;
	
}
