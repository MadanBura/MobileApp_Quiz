package com.ex.quizapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.quizapp.dao.CategoryDao;
import com.ex.quizapp.dto_classes.QuestionDto;
import com.ex.quizapp.model.Question;
import com.ex.quizapp.services.QuestionService;

@RestController
@RequestMapping("questions")
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
		

	@GetMapping("/allQuestions")
	public ResponseEntity<List<Question>> getAllQuestion(){
		return questionService.getAllQuestions();
	}
	

	@GetMapping("/category/{category}")
	public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category){
		return questionService.getQuestionsByCategory(category);
	}
	

	@PostMapping("/add")
	public ResponseEntity<String> addQuestion(@RequestBody QuestionDto question) {
		return questionService.addQuestion(question);
	}
	
	@PostMapping("/addAll")
	public ResponseEntity<String> saveAllQuestion(@RequestBody List<QuestionDto> questionList) {
		return questionService.saveAllQuestions(questionList);
	}
	
	
}
