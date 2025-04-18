package com.ex.quizapp.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ex.quizapp.dao.ResultDao;
import com.ex.quizapp.model.Result;
import java.util.List;

@Service
public class ResultService {

    @Autowired
    private ResultDao resultRepository;

    public List<Result> getResultsByUserId(Integer userId) {
        return resultRepository.findByUserId(userId);
    }
}
