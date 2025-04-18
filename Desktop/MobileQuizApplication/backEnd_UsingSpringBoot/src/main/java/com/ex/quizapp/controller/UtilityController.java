package com.ex.quizapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ex.quizapp.dao.CategoryDao;
import com.ex.quizapp.dao.TopicDao;
import com.ex.quizapp.dto_classes.CategoryDto;
import com.ex.quizapp.dto_classes.QuestionDto;
import com.ex.quizapp.dto_classes.TopicDto;
import com.ex.quizapp.model.Category;
import com.ex.quizapp.model.Question;
import com.ex.quizapp.model.Topic;
import com.ex.quizapp.services.CommonService;
import com.ex.quizapp.services.QuestionService;

@RestController
@RequestMapping("/adding")
public class UtilityController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommonService commonService;
    
    
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private TopicDao topicDao;

    //ok
    // Add a single question
    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody QuestionDto question) {
        return questionService.addQuestion(question);
    }

    // Add multiple questions
    @PostMapping("/addAll")
    public ResponseEntity<String> saveAllQuestion(@RequestBody List<QuestionDto> questionList) {	
    	 questionService.saveAllQuestions(questionList); // Now safe to persist
         return ResponseEntity.ok("All Questions added successfully");
    	
    }

    // Add a single category
    @PostMapping("/addOneCategory")
    public ResponseEntity<String> saveOneCategory(@RequestBody CategoryDto category) {
        return commonService.addOneCategory(category);
    }

    // Add multiple categories
    @PostMapping("/addAllCategories")
    public ResponseEntity<String> saveAllCategories(@RequestBody List<CategoryDto> categoryList) {
        return commonService.addAllCategories(categoryList);
    }

    // Add a single topic
    @PostMapping("/addOneTopic")
    public ResponseEntity<String> saveOneTopic(@RequestBody TopicDto topic) {
        return commonService.addOneTopic(topic);
    }

    // Add multiple topics
    @PostMapping("/addAllTopics")
    public ResponseEntity<String> saveAllTopics(@RequestBody List<TopicDto> topicList) {
        commonService.addAllTopics(topicList); // Now safe to persist
        return ResponseEntity.ok("Topics added successfully");
    }
    
    @PostMapping("/addTopic")
    public ResponseEntity<Topic> addTopic(@RequestParam String topicName, @RequestParam Integer categoryId) {
        Topic savedTopic = commonService.saveTopic(topicName, categoryId);
        return ResponseEntity.ok(savedTopic);
    }

    
}
