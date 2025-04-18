package com.ex.quizapp.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ex.quizapp.dao.CategoryDao;
import com.ex.quizapp.dao.QuestionDao;
import com.ex.quizapp.dao.TopicDao;
import com.ex.quizapp.dto_classes.QuestionDto;
import com.ex.quizapp.model.Category;
import com.ex.quizapp.model.Question;
import com.ex.quizapp.model.Topic;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionDao questionDao;
    
    
    @Autowired
	private CategoryDao categoryDao;
    
    @Autowired
    private TopicDao topicDao;
    
    // Get all questions
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            List<Question> questions = questionDao.findAll();
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get questions by category
    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            List<Question> questions = questionDao.findByCategory(category);
            if (questions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Add a single question
    public ResponseEntity<String> addQuestion(QuestionDto question) {
        try {
        	
        	 Category category = categoryDao.findById(question.getCategoryId())
                     .orElseThrow(() -> new RuntimeException("Category not found with id: " + question.getCategoryId()));
             Topic topic = topicDao.findById(question.getCategoryId())
                     .orElseThrow(() -> new RuntimeException("Topic not found with id: " + question.getTopicId()));

             // Map DTO to Entity
             Question ques = new Question();
             ques.setCategory(category);
             ques.setTopic(topic);
             ques.setQuestion_title(question.getQuestionTitle());
             ques.setOption1(question.getOption1());
             ques.setOption2(question.getOption2());
             ques.setOption3(question.getOption3());
             ques.setOption4(question.getOption4());
             ques.setRightAnswer(question.getRightAnswer());
             ques.setDifficultyLevel(question.getDifficultyLevel());

            questionDao.save(ques);
            return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add question", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Add multiple questions
    public ResponseEntity<String> saveAllQuestions(List<QuestionDto> questions) {
        try {
        	  List<Question> questionEntities = new ArrayList<>();

              for (QuestionDto dto : questions) {
                  // Fetch related Category and Topic
                  Category category = categoryDao.findById(dto.getCategoryId())
                          .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
                  Topic topic = topicDao.findById(dto.getTopicId())  // Use dto.getTopicId() here
                          .orElseThrow(() -> new RuntimeException("Topic not found with id: " + dto.getTopicId()));

                  // Map DTO to Entity
                  Question question = new Question();
                  question.setCategory(category);
                  question.setTopic(topic);
                  question.setQuestion_title(dto.getQuestionTitle());
                  question.setOption1(dto.getOption1());
                  question.setOption2(dto.getOption2());
                  question.setOption3(dto.getOption3());
                  question.setOption4(dto.getOption4());
                  question.setRightAnswer(dto.getRightAnswer());
                  question.setDifficultyLevel(dto.getDifficultyLevel());

                  questionEntities.add(question);
              }

              // Save all questions
              questionDao.saveAll(questionEntities);

              return new ResponseEntity<>("All questions added successfully", HttpStatus.CREATED);
          } catch (Exception e) {
              e.printStackTrace();
              return new ResponseEntity<>("Failed to add questions: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
          }
    }
}
