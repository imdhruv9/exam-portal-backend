package com.testportal.online_test_portal.service.implementation;

import com.testportal.online_test_portal.dto.OptionDto;
import com.testportal.online_test_portal.dto.QuestionRequestDto;
import com.testportal.online_test_portal.entity.Option;
import com.testportal.online_test_portal.entity.Question;
import com.testportal.online_test_portal.entity.Topic;
import com.testportal.online_test_portal.entity.User;
import com.testportal.online_test_portal.exception.custom.UserNotFoundException;
import com.testportal.online_test_portal.repository.OptionRepository;
import com.testportal.online_test_portal.repository.QuestionRepository;
import com.testportal.online_test_portal.repository.TopicRepository;
import com.testportal.online_test_portal.repository.UserRepository;
import com.testportal.online_test_portal.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, UserRepository userRepository, TopicRepository topicRepository, OptionRepository optionRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.optionRepository = optionRepository;
    }

    @Override
    public void addQuestion(QuestionRequestDto questionRequestDto) {
        Optional<User> optionalUser =  userRepository.findById(questionRequestDto.getUserId());
        User user = optionalUser.orElseThrow(()-> new UserNotFoundException("Please enter Valid user"));

        Optional<Topic> optionalTopic = topicRepository.findById(questionRequestDto.getTopicId());
        Topic topic = optionalTopic.orElseThrow(()-> new UserNotFoundException("please Enter valid topic id"));

        Question question = Question.builder()
                .questionText(questionRequestDto.getQuestionText())
                .topic(topic)
                .createdBy(user)
                .build();
        question = questionRepository.save(question);

        for (OptionDto option : questionRequestDto.getOptions()){
            Option option1 = Option.builder()
                    .optionText(option.getText())
                    .isCorrect(option.isCorrect())
                    .question(question)
                    .build();
            optionRepository.save(option1);
        }


    }
}
