package com.testportal.online_test_portal.controller;

import com.testportal.online_test_portal.dto.ExamRequestDto;
import com.testportal.online_test_portal.dto.ExamResponseDto;
import com.testportal.online_test_portal.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exams")
public class ExamController {
    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<ExamResponseDto> createExam(@RequestBody @Valid ExamRequestDto requestDto){
        ExamResponseDto responseDto = examService.createExam(requestDto);
        return  new ResponseEntity<>(responseDto, HttpStatus.CREATED);

    }

}
