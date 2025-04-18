package com.ex.quizapp.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ex.quizapp.dao.CategoryDao;
import com.ex.quizapp.dao.QuestionDao;
import com.ex.quizapp.dao.QuizDao;
import com.ex.quizapp.dao.ResultDao;
import com.ex.quizapp.dao.TopicDao;
import com.ex.quizapp.dao.UserDao;
import com.ex.quizapp.dto_classes.CategoryResponseDto;
import com.ex.quizapp.dto_classes.ResultResponseDto;
import com.ex.quizapp.dto_classes.TopicDto;
import com.ex.quizapp.model.Category;
import com.ex.quizapp.model.Question;
import com.ex.quizapp.model.QuestionWrapper;
import com.ex.quizapp.model.QuestionWrapperTopic;
import com.ex.quizapp.model.Quiz;
import com.ex.quizapp.model.QuizResponse;
import com.ex.quizapp.model.Result;
import com.ex.quizapp.model.Topic;
import com.ex.quizapp.model.User;
import com.ex.quizapp.model.UserResponse;
import com.ex.quizapp.model.UserScreenResponse;

@Service
public class QuizService {

	@Autowired
	private QuizDao quizDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private ResultDao resultDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private CategoryDao categoryDao;
	
	
	public ResponseEntity<List<TopicDto>> getTopicsByCategory(Integer categoryId) {
	        try {
	            List<Topic> topics = topicDao.findByCategoryId(categoryId);
	            
	            List<TopicDto> topicDtoList = new ArrayList<>();
	            for(Topic t : topics) {
	            	TopicDto topic = new TopicDto();
	            	topic.setCategoryId(t.getCategory().getId());
	            	topic.setId(t.getId());
	            	topic.setName(t.getName());
	            	
	            	
	            	topicDtoList.add(topic);
	            }
	           	            
	            return new ResponseEntity<>(topicDtoList, HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	}
	
	
	  public ResponseEntity<List<Question>> createQuizByTopic(String topicName) {
	        try {
	            Topic topic = topicDao.findByName(topicName).get();
	            if (topic == null) {
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            }
	            List<Question> questions = questionDao.findByTopicId(topic.getId());
	            return new ResponseEntity<>(questions, HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	
	public ResponseEntity<List<Category>> getAllCategories() {
	    try {
//	        List<String> categories = questionDao.findAllCategories();
//	        if (categories.isEmpty()) {
//	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	        }
	    	
	    	List<Category> categoryList = categoryDao.findAll();
	        
	    	 if (categoryList.isEmpty()) {
		            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		        }
	        
	        return new ResponseEntity<>(categoryList, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	public ResponseEntity<QuizResponse> createQuiz(Integer categoryId, int no, String title, String time) {
	    // Fetch random questions
	    List<Question> questions = questionDao.findRandomQuestionByCategory(categoryId, no);

	    // Create Quiz object
	    Quiz quizObj = new Quiz();
	    quizObj.setTitle(title);
	    quizObj.setQuestion(questions);
	    quizObj.setTime(time.toLowerCase());
	    // ✅ Save quiz to DB
	    quizObj = quizDao.save(quizObj);

	    return new ResponseEntity<>(new QuizResponse(quizObj, "Quiz Created Successfully"), HttpStatus.CREATED);
	}


	public ResponseEntity<List<QuestionWrapper>> getQuizQuestionById(Integer no) {
		Optional<Quiz> quiz = quizDao.findById(no);
		
		List<Question> questions = quiz.get().getQuestion();
		
		List<QuestionWrapper> questionWrappers = new ArrayList<>();
		
		for(Question q : questions) {
			questionWrappers.add(new QuestionWrapper(q.getId(),q.getCategory().getName(), q.getQuestion_title(), q.getOption1(), q.getOption2(), q.getOption3(),q.getOption4()));
		}
		
		return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
	}

//	public ResponseEntity<Integer> calculateResult(int id, List<UserResponse> response) {
//	    try {
//	       
//	        Quiz quiz = quizDao.findById(id).orElse(null);
//
//	        if (quiz == null) {
//	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	        }
//
//	        List<Question> questions = quiz.getQuestion(); 
//	        if (questions.size() != response.size()) {
//	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	        }
//
//	        int correctAnswers = 0;
//	        for (int i = 0; i < response.size(); i++) {
//	            if (response.get(i).getUserAnswer().equalsIgnoreCase(questions.get(i).getRightAnswer())) {
//	                correctAnswers++;
//	            }
//	        }
//	        return new ResponseEntity<>(correctAnswers, HttpStatus.OK);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
//	}
	
	public ResponseEntity<ResultResponseDto> calculateResult(int quizId, List<UserResponse> response) {
	    try {
	        // Fetch the quiz from the database
	        Quiz quiz = quizDao.findById(quizId).orElse(null);
	        if (quiz == null) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        // Validate response size
	        List<Question> questions = quiz.getQuestion();
	        if (questions.size() != response.size()) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }

	        int correctAnswers = 0;
	        Integer userId = null; // Extract user ID from the response
	        for (int i = 0; i < response.size(); i++) {
	            if (response.get(i).getUserAnswer().equalsIgnoreCase(questions.get(i).getRightAnswer())) {
	                correctAnswers++;
	            }

	            // Extract userId from the first response (assuming all answers belong to the same user)
	            if (userId == null) {
	                userId = response.get(0).getUserId(); // Add userId in UserResponse model
	            }
	        }

	        // Fetch user from the database
	        if (userId == null) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }

	        User user = userDao.findById(userId).orElse(null);
	        if (user == null) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	        
	 
	        // Create and save the result
	        Result result = new Result();
	        result.setUser(user);
	        result.setQuiz(quiz);
	        result.setScore(correctAnswers);
	        result.setTotalQuestions(questions.size());
	        result.setDate(LocalDateTime.now());

	        result = resultDao.save(result); // Save result in DB

	        ResultResponseDto resultDto = new ResultResponseDto(result.getQuiz().getId(),result.getQuiz().getTitle(),result.getScore(), result.getTotalQuestions(), result.getDate()); 
	        
	        
	        return new ResponseEntity<>(resultDto, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	 public List<QuestionWrapperTopic> getQuizQuestionsByTopic(Integer topicId) {
	        // Fetch the topic by ID (you may want to handle this with error handling)
	        Topic topic = topicDao.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));

	        // Fetch the questions for the given topic
	        List<Question> questions = questionDao.findByTopicId(topic.getId());

	        // Wrap questions into a QuestionWrapper (adjust this if needed for your response format)
	        List<QuestionWrapperTopic> questionWrappers = questions.stream()
	                .map(q -> new QuestionWrapperTopic(q.getId(),topic.getName(), q.getQuestion_title(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4()))
	                .collect(Collectors.toList());

	        return questionWrappers;
	    }
	
	public ResponseEntity<List<Quiz>> getAllQuizes(){
		try {
			return new ResponseEntity<>(quizDao.findAll(), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
		}
	}
	
	public Topic getTopicById(Integer topicId) {
	    return topicDao.findById(topicId).orElse(null);
	}
	
}
