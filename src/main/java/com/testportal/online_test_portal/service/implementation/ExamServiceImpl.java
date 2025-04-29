package com.testportal.online_test_portal.service.implementation;

import com.testportal.online_test_portal.dto.ExamRequestDto;
import com.testportal.online_test_portal.dto.ExamResponseDto;
import com.testportal.online_test_portal.entity.Exam;
import com.testportal.online_test_portal.entity.Topic;
import com.testportal.online_test_portal.entity.User;
import com.testportal.online_test_portal.exception.custom.ResourceNotFoundException;
import com.testportal.online_test_portal.exception.custom.UserNotFoundException;
import com.testportal.online_test_portal.repository.ExamRepository;
import com.testportal.online_test_portal.repository.TopicRepository;
import com.testportal.online_test_portal.repository.UserRepository;
import com.testportal.online_test_portal.service.ExamService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, UserRepository userRepository, TopicRepository topicRepository) {
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }
    @Transactional
    @Override
    public ExamResponseDto createExam(ExamRequestDto requestDto){
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(()-> new UserNotFoundException("No user found"));
        Topic topic = topicRepository.findById(requestDto.getTopicId())
                .orElseThrow(()-> new ResourceNotFoundException("Please Enter a valid topic id"));
        Exam exam = Exam.builder()
                .topic(topic)
                .noOfQuestion(requestDto.getNoOfQuestion())
                .createdBy(user)
                .build();
        exam = examRepository.save(exam);
        return ExamResponseDto.builder()
                .id(exam.getId())
                .noOfQuestion(exam.getNoOfQuestion())
                .topicName(exam.getTopic().getName())
                .createdBy(exam.getCreatedBy().getFirstName()+ " " +exam.getCreatedBy().getLastName())
                .createdDate(exam.getCreatedDate())
                .build();


    }
}
