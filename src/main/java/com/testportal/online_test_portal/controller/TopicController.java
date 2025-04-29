package com.testportal.online_test_portal.controller;

import com.testportal.online_test_portal.dto.TopicDto;
import com.testportal.online_test_portal.repository.TopicRepository;
import com.testportal.online_test_portal.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topics")
public class TopicController {
    private final TopicService topicService;
    private final TopicRepository topicRepository;

    @Autowired
    public TopicController(TopicService topicService, TopicRepository topicRepository) {
        this.topicService = topicService;
        this.topicRepository = topicRepository;
    }

    @PostMapping()
    public ResponseEntity<TopicDto> addTopic(@RequestBody @Valid TopicDto topicDto){
        TopicDto topic = topicService.addTopic(topicDto);
        return  new ResponseEntity<>(topic, HttpStatus.CREATED);
    }
}
