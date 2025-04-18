package com.ex.quizapp.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserScreenResponse {
	 private Integer id;   
	 private String fullname;
	 private String email;
}
