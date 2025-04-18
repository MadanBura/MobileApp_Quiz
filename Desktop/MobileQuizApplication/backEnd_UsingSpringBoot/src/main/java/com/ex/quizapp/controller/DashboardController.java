package com.ex.quizapp.controller;

import com.ex.quizapp.dto_classes.CategoryResponseDto;
import com.ex.quizapp.dto_classes.QuizDtoResponse;
import com.ex.quizapp.dto_classes.ResultResponseDto;
import com.ex.quizapp.model.Category;
import com.ex.quizapp.model.Quiz;
import com.ex.quizapp.model.Result;
import com.ex.quizapp.model.User;
import com.ex.quizapp.model.UserDetailResponse;
import com.ex.quizapp.model.UserScreenResponse;
import com.ex.quizapp.services.CommonService;
import com.ex.quizapp.services.JwtService;
import com.ex.quizapp.services.QuizService;
import com.ex.quizapp.services.ResultService;
import com.ex.quizapp.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private QuizService quizService;

    @Autowired
    private ResultService resultService;
    
    @Autowired
    private JwtService jwtService;

//    @GetMapping("/{userId}")
//    public ResponseEntity<Map<String, Object>> getDashboardData(@PathVariable Integer userId) {
//        Map<String, Object> dashboardData = new HashMap<>();
//
//        // Fetch user profile
//        User user = userService.getUserById(userId);
//        if (user == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
//        }
//
//        // Fetch categories
//        List<String> categories = quizService.getAllCategories().getBody();
//
//        // Fetch quizzes
//        List<Quiz> quizzes = quizService.getAllQuizes().getBody();
//
//        // Fetch previous results
//        List<Result> results = resultService.getResultsByUserId(userId);
//
//        // Populate response
//        dashboardData.put("userProfile", user);
//        dashboardData.put("categories", categories);
//        dashboardData.put("quizzes", quizzes);
//        dashboardData.put("previousResults", results);
//
//        return ResponseEntity.ok(dashboardData);
//    }
    
  
    @GetMapping("/getUserDetail")
    public ResponseEntity<UserDetailResponse> getDashboardData(@RequestHeader("Authorization") String token) {
        // Extract JWT token (Remove "Bearer " prefix)
        System.out.println("FrontEndtoken: " + token);
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt); // Extract email from token

        // Fetch user by email
        User user = userService.getUserByUserName(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Fetch categories
//        List<String> categories = quizService.getAllCategories().getBody();
        List<Category> categoryList = quizService.getAllCategories().getBody();
        if (categoryList == null || categoryList.isEmpty()) {
	        return ResponseEntity.noContent().build(); 
	    }

	    List<CategoryResponseDto> categoryResponseDtosList = categoryList.stream()
	            .map(category -> new CategoryResponseDto(
	                    category.getId(),
	                    category.getName()
	            )).collect(Collectors.toList());
        
        // Fetch quizzes
        List<Quiz> quizzes = quizService.getAllQuizes().getBody();
        List<QuizDtoResponse> quizDtoList = quizzes.stream()
                .map(quiz -> new QuizDtoResponse(
                    quiz.getId(),
                    quiz.getTitle(),
                    quiz.getQuestion().size(),
                    quiz.getTime()
                ))
                .collect(Collectors.toList());
        
        // Fetch previous results
        List<Result> results = resultService.getResultsByUserId(user.getId());

        // Convert User entity to response DTO
        UserScreenResponse userRes = new UserScreenResponse(user.getId(), user.getFullname(), user.getEmail());

        // Convert List<Result> to List<ResultResponseDTO>
        List<ResultResponseDto> resultDtoList = results.stream()
            .map(result -> new ResultResponseDto(
            		result.getQuiz().getId(),
            		result.getQuiz().getTitle(),
                result.getScore(),
                result.getTotalQuestions(),
                result.getDate()
            ))
            .collect(Collectors.toList());

        // Create response object
        UserDetailResponse dashboardResponse = new UserDetailResponse(userRes, categoryResponseDtosList, quizDtoList, resultDtoList);

        return ResponseEntity.ok(dashboardResponse);
    }

}
