package com.ex.quizapp.dto_classes;

import lombok.Data;
import java.util.List;

@Data
public class CategoryDto {
    private Integer id;
    private String name;
//
//    // Optional: you can include topic info if needed
//    private List<TopicDto> topics;
}