package com.ex.quizapp.controller;

import com.ex.quizapp.model.LoginRequest;
import com.ex.quizapp.model.LoginResponse;
import com.ex.quizapp.model.RegisterRequest;
import com.ex.quizapp.model.RegistrationResponse;
import com.ex.quizapp.model.User;
import com.ex.quizapp.services.JwtService;
import com.ex.quizapp.services.TokenBlacklistService;
import com.ex.quizapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegisterRequest request) {
    	
    	  if (request.getEmail() == null) {
    	        return ResponseEntity.badRequest().body(new RegistrationResponse(HttpStatus.BAD_REQUEST.toString(), "Email is required"));
    	    }
    	    
    	
        User user = new User();
        user.setFullname(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userService.registerUser(user);

        RegistrationResponse res; 
        if (savedUser != null) {
            res = new RegistrationResponse(HttpStatus.OK.toString(), "Successfully registered, Please Login");
            return ResponseEntity.ok(res);
        } else {
            res = new RegistrationResponse(HttpStatus.BAD_REQUEST.toString(), "Registration failed. Please try again.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

        System.out.println("JWT TOKEN : "+ jwtToken);
        return ResponseEntity.ok(new LoginResponse(HttpStatus.OK.value(), jwtToken, "Login successful!"));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix
        String jwt = token.substring(7);
        tokenBlacklistService.blacklistToken(jwt);
        return ResponseEntity.ok("Logged out successfully!");
    }
}
