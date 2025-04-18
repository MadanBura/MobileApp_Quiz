package com.ex.quizapp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ex.quizapp.model.Topic;


@Repository
public interface TopicDao extends JpaRepository<Topic, Integer> {
    List<Topic> findByCategoryId(Integer categoryId);
    Topic findTopicByCategoryId(Integer categoryId);
    Optional<Topic> findByName(String name);
}