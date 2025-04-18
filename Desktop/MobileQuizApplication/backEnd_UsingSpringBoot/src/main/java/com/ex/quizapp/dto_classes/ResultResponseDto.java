package com.ex.quizapp.dto_classes;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ResultResponseDto {
	private Integer quizId;
	private String quizName;
    private Integer score;
    private Integer totalQuestions;
    private LocalDateTime date;
}