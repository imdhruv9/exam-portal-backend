package com.testportal.online_test_portal.service.implementation;

import com.testportal.online_test_portal.dto.TopicDto;
import com.testportal.online_test_portal.entity.Topic;
import com.testportal.online_test_portal.entity.User;
import com.testportal.online_test_portal.exception.custom.UserNotFoundException;
import com.testportal.online_test_portal.repository.TopicRepository;
import com.testportal.online_test_portal.repository.UserRepository;
import com.testportal.online_test_portal.service.TopicService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public TopicServiceImpl(TopicRepository topicRepository, UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TopicDto addTopic(TopicDto topicDto){
       Optional<User> optionalUser= userRepository.findById(topicDto.getUserId());
       User user = optionalUser.orElseThrow(()->  new UserNotFoundException("User not found"));


        Topic topic = Topic.builder()
                .name(topicDto.getName())
                .createdBy(user)
                .build();
         Topic savedTopic = topicRepository.save(topic);
         return TopicDto.builder()
                 .name(savedTopic.getName())
                 .userId(savedTopic.getCreatedBy().getId())
                 .build();
    }
}
