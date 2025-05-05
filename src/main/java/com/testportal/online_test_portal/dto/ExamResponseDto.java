package com.testportal.online_test_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponseDto {
    private Long id;
    private Integer noOfQuestion;
    private String topicName;
    private String createdBy;
    private LocalDate createdDate;
}

