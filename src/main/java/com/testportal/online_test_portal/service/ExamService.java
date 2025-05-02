package com.testportal.online_test_portal.service;

import com.testportal.online_test_portal.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExamService {
    ExamResponseDto createExam(ExamRequestDto requestDto);

    List<ExamResponseDto> fetchExams();

    ExamStartResDto startExam(Long userID, Long examId);

    SubmitExamResDto submitExam(Long id, SubmitExamReqDto reqDto);

    ResultResponseDto viewResult(Long userExamId);
}
