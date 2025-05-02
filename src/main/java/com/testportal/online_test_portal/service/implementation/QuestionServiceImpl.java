package com.testportal.online_test_portal.service.implementation;

import com.testportal.online_test_portal.dto.OptionDto;
import com.testportal.online_test_portal.dto.QuestionListDto;
import com.testportal.online_test_portal.dto.QuestionRequestDto;
import com.testportal.online_test_portal.entity.Option;
import com.testportal.online_test_portal.entity.Question;
import com.testportal.online_test_portal.entity.Topic;
import com.testportal.online_test_portal.entity.User;
import com.testportal.online_test_portal.exception.custom.ResourceNotFoundException;
import com.testportal.online_test_portal.exception.custom.UserNotFoundException;
import com.testportal.online_test_portal.repository.OptionRepository;
import com.testportal.online_test_portal.repository.QuestionRepository;
import com.testportal.online_test_portal.repository.TopicRepository;
import com.testportal.online_test_portal.repository.UserRepository;
import com.testportal.online_test_portal.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @Override
    public void addMultipleQuestion(QuestionListDto questionListDto){
        for(QuestionRequestDto requestDto : questionListDto.getQuestions()){
            User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(()-> new UserNotFoundException("Please enter valid user id"));
            Topic topic = topicRepository.findById(requestDto.getTopicId())
                    .orElseThrow(()-> new ResourceNotFoundException("Please enter valid topic id"));

            Question question = Question.builder()
                    .questionText(requestDto.getQuestionText())
                    .topic(topic)
                    .createdBy(user)
                    .build();
            question = questionRepository.save(question);

            for (OptionDto dto: requestDto.getOptions()){
                Option option = Option.builder()
                        .optionText(dto.getText())
                        .question(question)
                        .isCorrect(dto.isCorrect())
                        .build();
                optionRepository.save(option);
            }
        }
    }
    @Override
    public void addQuestionFromCsv(MultipartFile file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        boolean isFirstLine = true;
        while ((line = br.readLine())!= null){
            if(isFirstLine) {
                isFirstLine = false;
                continue;
            }
            String[] data = line.split(",",-1);
            if(data.length < 8) continue;
            String questionText = data[0];
            String[] options = Arrays.copyOfRange(data,1,5);
            String correctLetter = data[5].trim();
            Long topicId = Long.parseLong(data[6].trim());
            Long userId = Long.parseLong(data[7].trim());
            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(()-> new ResourceNotFoundException("Please Input valid topic id"));
            User user = userRepository.findById(userId)
                    .orElseThrow(()->new UserNotFoundException("please enter valid userId"));
            Question question = Question.builder()
                    .questionText(questionText)
                    .topic(topic)
                    .createdBy(user)
                    .build();

            String[] label = {"A","B","C","D"};
            List<Option> optionList = new ArrayList<>();

            for(int i=0; i<4; i++){
               boolean isCorrect = label[i].equalsIgnoreCase(correctLetter);
               Option option = Option.builder()
                       .optionText(options[i])
                       .isCorrect(isCorrect)
                       .question(question)
                       .build();
               optionList.add(option);

            }
            question.setOptions(optionList);
            questionRepository.save(question);


        }

    }
}
