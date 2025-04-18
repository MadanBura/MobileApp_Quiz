package com.ex.quizapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ex.quizapp.model.Question;

@Repository
public interface QuestionDao extends JpaRepository<Question, Long>{
	
	
	@Query("SELECT q FROM questions q WHERE q.category.name = :categoryName")
	List<Question> findByCategory(String categoryName);
	
	  @Query("SELECT DISTINCT q.category.name FROM questions q")
	    List<String> findAllCategories(); 
	
	  @Query(value = "SELECT * FROM questions q WHERE q.category_id = :category ORDER BY RAND() LIMIT :numOfQues", nativeQuery = true)
	  List<Question> findRandomQuestionByCategory(@Param("category") Integer category, @Param("numOfQues") int numOfQues);

	 List<Question> findByTopicId(Integer topicId);
}
