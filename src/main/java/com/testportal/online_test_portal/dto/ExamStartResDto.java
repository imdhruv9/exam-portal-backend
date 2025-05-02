package com.testportal.online_test_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ExamStartResDto {
    private Long userExamId;
    private String topic;
    private List<ExamQuestionResDto> questions;
}
