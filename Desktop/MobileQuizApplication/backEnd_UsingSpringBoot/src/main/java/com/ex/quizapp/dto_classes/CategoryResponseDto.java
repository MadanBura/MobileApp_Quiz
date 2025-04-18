package com.ex.quizapp.dto_classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryResponseDto {
	
	private int id;
	private String categoryName;

}
