package com.ex.quizapp.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ex.quizapp.dao.CategoryDao;
import com.ex.quizapp.dao.TopicDao;
import com.ex.quizapp.dto_classes.CategoryDto;
import com.ex.quizapp.dto_classes.QuestionDto;
import com.ex.quizapp.dto_classes.TopicDto;
import com.ex.quizapp.model.Category;
import com.ex.quizapp.model.Question;
import com.ex.quizapp.model.Topic;

@Service
public class CommonService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TopicDao topicDao;

    // Add a single category
    public ResponseEntity<String> addOneCategory(CategoryDto categoryDto) {
    	 try {
    	        Category category = new Category();
    	        category.setName(categoryDto.getName());

    	        categoryDao.save(category);
    	        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        return new ResponseEntity<>("Failed to add category", HttpStatus.INTERNAL_SERVER_ERROR);
    	    }
    }

    // Add multiple categories
    public ResponseEntity<String> addAllCategories(List<CategoryDto> categories) {
    	try {
            List<Category> categoryList = new ArrayList<>();
            
            for (CategoryDto categoryDto : categories) {
                Category category = new Category();
                category.setName(categoryDto.getName());
                categoryList.add(category);
            }

            categoryDao.saveAll(categoryList);
            return new ResponseEntity<>("All categories added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add categories", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add a single topic
    public ResponseEntity<String> addOneTopic(TopicDto topic) {
        try {
        	
        	Category category = categoryDao.findById(topic.getCategoryId()).get();
        		Topic top = new Topic();
        		top.setCategory(category);
        		top.setName(topic.getName());
            topicDao.save(top);
            return new ResponseEntity<>("Topic added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add topic", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add multiple topics
    public ResponseEntity<String> addAllTopics(List<TopicDto> topics) {
        try {
            // List to hold the topics to be saved
            List<Topic> topicList = new ArrayList<>();
            
            for (TopicDto t : topics) {
                // Fetch the category for each topic
                Category category = categoryDao.findById(t.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

                // Create a new topic and set its attributes
                Topic topic = new Topic();
                topic.setCategory(category); // Set the category for the topic
                topic.setName(t.getName());  // Set the topic name

                // Add the topic to the list
                topicList.add(topic);
            }

            // Save all topics to the database
            topicDao.saveAll(topicList);
            
            return new ResponseEntity<>("All topics added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add topics", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    
    public Topic saveTopic(String topicName, Integer categoryId) {
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Topic topic = new Topic();
        topic.setName(topicName);
        topic.setCategory(category); 
        return topicDao.save(topic); 
    }
    
    
   
    public ResponseEntity<Topic> addTopic(Topic topic) {
        Category category = topic.getCategory();
        
        if (category.getId() == null) {
            category = categoryDao.save(category);
        }
        
        topic.setCategory(category);
        Topic savedTopic = topicDao.save(topic);
        
        return ResponseEntity.ok(savedTopic);
    }

    
}
