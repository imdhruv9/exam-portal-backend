package com.testportal.online_test_portal.service;

import com.testportal.online_test_portal.dto.ExamRequestDto;
import com.testportal.online_test_portal.dto.ExamResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ExamService {
    ExamResponseDto createExam(ExamRequestDto requestDto);
}
