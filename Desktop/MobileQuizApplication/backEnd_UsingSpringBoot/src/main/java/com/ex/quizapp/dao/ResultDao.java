package com.ex.quizapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ex.quizapp.model.Result;
import java.util.List;

public interface ResultDao extends JpaRepository<Result, Integer> {
    List<Result> findByUserId(Integer userId);
}

