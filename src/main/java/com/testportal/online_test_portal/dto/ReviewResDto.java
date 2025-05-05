package com.testportal.online_test_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResDto {
    private Long examId;
    private String topic;
    private  Integer totalQuestions;
    private Integer attemptedQuestions;
    private List<ReviewAnsDto> reviewAnsList;
}
