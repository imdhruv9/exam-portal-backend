package com.testportal.online_test_portal.service;

import com.testportal.online_test_portal.dto.QuestionRequestDto;
import com.testportal.online_test_portal.entity.Question;
import jakarta.validation.Valid;

public interface QuestionService {
    void addQuestion(@Valid QuestionRequestDto questionRequestDto);
}
