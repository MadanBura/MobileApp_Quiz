

package com.ex.quizapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ex.quizapp.dto_classes.CategoryResponseDto;
import com.ex.quizapp.dto_classes.ResultResponseDto;
import com.ex.quizapp.dto_classes.TopicDto;
import com.ex.quizapp.model.Category;
import com.ex.quizapp.model.Question;
import com.ex.quizapp.model.QuestionWrapper;
import com.ex.quizapp.model.QuestionWrapperTopic;
import com.ex.quizapp.model.QuizRequest;
import com.ex.quizapp.model.QuizResponse;
import com.ex.quizapp.model.Result;
import com.ex.quizapp.model.Topic;
import com.ex.quizapp.model.UserResponse;
import com.ex.quizapp.services.QuizService;

import io.swagger.v3.oas.annotations.Operation;

@RequestMapping("/quiz")
@RestController
public class QuizController {
	
	@Autowired
	private QuizService quizService;
	

	@Operation(summary = "Get All Subjects included in App")
	@GetMapping("/getCatgories")
	public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {

	    List<Category> categoryList = quizService.getAllCategories().getBody();

	    if (categoryList == null || categoryList.isEmpty()) {
	        return ResponseEntity.noContent().build(); 
	    }

	    List<CategoryResponseDto> categoryResponseDtosList = categoryList.stream()
	            .map(category -> new CategoryResponseDto(
	                    category.getId(),
	                    category.getName()
	            )).collect(Collectors.toList());

	    return ResponseEntity.ok(categoryResponseDtosList);
	}

	
	@PostMapping("/create")
	public ResponseEntity<QuizResponse> createQuiz(@RequestBody QuizRequest quizRequest){
		return quizService.createQuiz(quizRequest.getCategoryid(),quizRequest.getNo(),quizRequest.getTitle(), quizRequest.getTime());
	}
	
	
	@GetMapping("/getQuiz/{id}")
	public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable int id){
		return quizService.getQuizQuestionById(id);
	}
	
	@PostMapping("/submit/{id}")
	public ResponseEntity<ResultResponseDto> submitQuiz(@PathVariable int id, @RequestBody List<UserResponse> response){
		return quizService.calculateResult(id,response);
	}
	
	 @GetMapping("/topics/{id}")
	 public ResponseEntity<List<TopicDto>> getTopicsByCategory(@PathVariable("id") Integer categoryId) {
	        return quizService.getTopicsByCategory(categoryId);
	 }

	 @GetMapping("/create")
	 public ResponseEntity<List<Question>> createQuizByTopic(@RequestParam String topicName) {
	        return quizService.createQuizByTopic(topicName);
	 }
		
	 
	 @GetMapping("/createQuizByTopic/{id}")
	    public ResponseEntity<List<QuestionWrapperTopic>> createQuizByTopic(@PathVariable("id") Integer topicId) {
	        // Fetch questions for the given topic ID
	        List<QuestionWrapperTopic> questions = quizService.getQuizQuestionsByTopic(topicId);

	        if (questions == null || questions.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }

	        return ResponseEntity.ok(questions);
	    }
	 
	 
}
