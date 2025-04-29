package com.testportal.online_test_portal.controller;

import com.testportal.online_test_portal.dto.QuestionRequestDto;
import com.testportal.online_test_portal.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    @PostMapping
    public ResponseEntity<String> addQuestion(@RequestBody @Valid QuestionRequestDto questionRequestDto){
         questionService.addQuestion(questionRequestDto);
        return new ResponseEntity<>("Question uploaded successfully", HttpStatus.CREATED);
    }
}