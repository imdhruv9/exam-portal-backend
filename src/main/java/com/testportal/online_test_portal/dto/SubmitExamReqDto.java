package com.testportal.online_test_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SubmitExamReqDto{
    private Map<Long,String> answers;
}
