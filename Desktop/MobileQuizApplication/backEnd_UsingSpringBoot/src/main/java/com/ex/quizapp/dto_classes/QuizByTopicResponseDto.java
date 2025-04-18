package com.ex.quizapp.dto_classes;


import java.util.List;
import com.ex.quizapp.model.QuestionWrapperTopic;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuizByTopicResponseDto {
    private Integer totalQues;
    private String time;
    private List<QuestionWrapperTopic> questions;
}