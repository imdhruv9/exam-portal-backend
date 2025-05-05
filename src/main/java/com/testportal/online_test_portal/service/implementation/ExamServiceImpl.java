package com.testportal.online_test_portal.service.implementation;

import com.testportal.online_test_portal.dto.*;
import com.testportal.online_test_portal.entity.*;
import com.testportal.online_test_portal.exception.custom.ResourceNotFoundException;
import com.testportal.online_test_portal.exception.custom.UserNotFoundException;
import com.testportal.online_test_portal.repository.*;
import com.testportal.online_test_portal.service.ExamService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final UserExamRepository userExamRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, UserRepository userRepository, TopicRepository topicRepository, UserExamRepository userExamRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.userExamRepository = userExamRepository;
        this.questionRepository = questionRepository;
    }
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
    @Override
    public List<ExamResponseDto> fetchExams(){
        List<Exam> exams = examRepository.findAll();
        List<ExamResponseDto> responseDtos = new ArrayList<>();
        for(Exam  exam : exams){
           ExamResponseDto responseDto = ExamResponseDto.builder()
                   .id(exam.getId())
                   .noOfQuestion(exam.getNoOfQuestion())
                   .topicName(exam.getTopic().getName())
                   .createdBy(exam.getCreatedBy().getFirstName()+" "+exam.getCreatedBy().getLastName())
                   .createdDate(exam.getCreatedDate())
                   .build();
           responseDtos.add(responseDto);
        }
        return responseDtos;
    }
    @Transactional
    @Override
    public ExamStartResDto startExam(Long userID, Long examId){
        User user = userRepository.findById(userID)
                .orElseThrow(()-> new UserNotFoundException("Please input valid user id"));
        Exam exam = examRepository.findById(examId)
                .orElseThrow(()-> new ResourceNotFoundException("please input valid exam id"));

        List<Question> allQuestions = questionRepository.findRandomQuestions(exam.getTopic().getId(),exam.getNoOfQuestion());

        Map<Long,List<String>> shuffledOptions = new HashMap<>();
        List<Long> questionIds = new ArrayList<>();
        List<ExamQuestionResDto> questionResDtos = new ArrayList<>();

        for(Question question : allQuestions){
            List<String> shuffled = new ArrayList<>();
            List<Option> options = question.getOptions();
            for(Option option : options){
                shuffled.add(option.getOptionText());
            }
            Collections.shuffle(shuffled);
            shuffledOptions.put(question.getId(),shuffled);
            questionIds.add(question.getId());
            questionResDtos.add(new ExamQuestionResDto(question.getId(),question.getQuestionText(),shuffled));

        }
        UserExam userExam = new UserExam();
        userExam.setUser(user);
        userExam.setExam(exam);
        userExam.setQuestionIds(questionIds);
        userExam.setShuffledOptions(shuffledOptions);
        userExam.setStartedAt(LocalDateTime.now());
        userExam = userExamRepository.save(userExam);
        return new ExamStartResDto(userExam.getId(),exam.getTopic().getName(),questionResDtos);

    }
    @Transactional
    @Override
    public SubmitExamResDto submitExam(Long id, SubmitExamReqDto reqDto){
        UserExam userExam = userExamRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Please Input valid id"));
        if (userExam.getSubmittedAt() != null) throw new IllegalStateException("Already submitted");
        List<Question> questions = questionRepository.findAllById(userExam.getQuestionIds());
        Integer correct = 0;
        for(Question q : questions){
            String correctAnswer = q.getOptions()
                    .stream()
                    .filter(Option::isCorrect)
                    .map(Option::getOptionText)
                    .findFirst()
                    .orElse("No correct answer found");
            String submittedAnswer = reqDto.getAnswers().get(q.getId());
            if (correctAnswer.equals(submittedAnswer)){
                correct ++;
            }

        }
        userExam.setUserAnswers(reqDto.getAnswers());
        userExam.setSubmittedAt(LocalDateTime.now());
        userExam.setCorrectAns(correct);
        userExamRepository.save(userExam);
        return new SubmitExamResDto
                ("Exam submitted successfully",questions.size(),correct,correct);
    }
    @Transactional
    @Override
    public ResultResponseDto viewResult(Long userExamId){
        UserExam  userExam = userExamRepository.findById(userExamId)
                .orElseThrow(()-> new ResourceNotFoundException("please input valid id"));
        List<Question> questions = questionRepository.findAllById(userExam.getQuestionIds());

        List<ResultBodyDto> result = new ArrayList<>();
        String status = "Incorrect";

        for(Question q : questions){
            ResultBodyDto resDto= new ResultBodyDto();
            resDto.setQuestion(q.getQuestionText());
            String submittedAns = userExam.getUserAnswers().get(q.getId());
            resDto.setSubmittedAns(submittedAns);
            String correctAns = q.getOptions()
                    .stream()
                    .filter(Option::isCorrect)
                    .map(Option::getOptionText).collect(Collectors.joining());
            resDto.setCorrectAns(correctAns);
            if(submittedAns.equals(correctAns)) status = "Correct";
            resDto.setStatus(status);
            result.add(resDto);
            status = "Incorrect";
        }
        return ResultResponseDto.builder()
               .topic(userExam.getExam().getTopic().getName())
               .totalQuestion(userExam.getExam().getNoOfQuestion())
               .totalCorrect(userExam.getCorrectAns())
               .score(userExam.getCorrectAns())
               .body(result)
               .build();

    }
    @Transactional
    @Override
    public List<PastExamsResDto> viewPastExams(Long userId){
          List<UserExam> userExams = userExamRepository.findByUserId(userId);
          List<PastExamsResDto> resDtos = new ArrayList<>();

          for(UserExam userExam: userExams ){
              PastExamsResDto pastExamsResDto = new PastExamsResDto();
              pastExamsResDto.setUserExamId(userExam.getId());
              pastExamsResDto.setTopic(userExam.getExam().getTopic().getName());
              pastExamsResDto.setTotalQues(userExam.getQuestionIds().size());
              pastExamsResDto.setCorrectAns(userExam.getCorrectAns());
              pastExamsResDto.setScore(userExam.getCorrectAns());
              resDtos.add(pastExamsResDto);
          }
          return resDtos;
    }
    @Transactional
    @Override
    public ReviewResDto reviewAnswers(Long userExamId, SubmitExamReqDto reqDto){
        Integer size = reqDto.getAnswers().size();
        UserExam userExam = userExamRepository.findById(userExamId)
                .orElseThrow(()->new ResourceNotFoundException("Please Enter Valid user Exam id"));
        List<Question> questions = questionRepository.findAllById(userExam.getQuestionIds());
        List<ReviewAnsDto> reviewAnsDtos = new ArrayList<>();
        for(Question question: questions){
            ReviewAnsDto reviewAnsDto = new ReviewAnsDto();
            reviewAnsDto.setQuestion(question.getQuestionText());
            reviewAnsDto.setSubmittedAns(reqDto.getAnswers().get(question.getId()));
            reviewAnsDtos.add(reviewAnsDto);
        }
        return ReviewResDto.builder()
                .examId(userExam.getExam().getId())
                .topic(userExam.getExam().getTopic().getName())
                .totalQuestions(userExam.getExam().getNoOfQuestion())
                .attemptedQuestions(size)
                .reviewAnsList(reviewAnsDtos)
                .build();
    }
}
