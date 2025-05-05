package com.testportal.online_test_portal.service;

import com.testportal.online_test_portal.dto.QuestionListDto;
import com.testportal.online_test_portal.dto.QuestionRequestDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface QuestionService {
    void addQuestion(@Valid QuestionRequestDto questionRequestDto);

    void addMultipleQuestion(QuestionListDto questionListDto);

    void addQuestionFromCsv(MultipartFile file) throws IOException;

    @Transactional
    void addQuestionFromExcel(MultipartFile file)throws IOException;
}
