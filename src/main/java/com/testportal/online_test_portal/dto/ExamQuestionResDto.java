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
public class ExamQuestionResDto {
    private Long QuesId;
    private String QuesText;
    private List<String> options;
}
