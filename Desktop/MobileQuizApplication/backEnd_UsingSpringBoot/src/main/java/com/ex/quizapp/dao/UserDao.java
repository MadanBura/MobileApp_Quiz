package com.ex.quizapp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.quizapp.model.User;

public interface UserDao extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);
}
