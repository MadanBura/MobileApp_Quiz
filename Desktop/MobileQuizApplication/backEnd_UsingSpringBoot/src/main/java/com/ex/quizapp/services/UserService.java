package com.ex.quizapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ex.quizapp.dao.UserDao;
import com.ex.quizapp.model.User;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userRepository;

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }
    
    public User getUserByUserName(String email) {
    	Optional<User> user= userRepository.findByEmail(email);
    	return user.get();
    }
    
   
    public User registerUser(User user) {
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
}