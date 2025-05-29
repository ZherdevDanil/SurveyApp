package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.Answer;
import com.example.surveyapp.Entity.Option;
import com.example.surveyapp.Entity.Question;
import com.example.surveyapp.Entity.User;
import com.example.surveyapp.Repository.AnswerRepository;
import com.example.surveyapp.Repository.OptionRepository;
import com.example.surveyapp.Repository.QuestionRepository;
import com.example.surveyapp.Repository.UserRepository;
import com.example.surveyapp.dto.AnswerRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService {

    public final AnswerRepository answerRepository;
    public final UserRepository userRepository;

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public AnswerService(AnswerRepository answerRepository, UserRepository userRepository, QuestionRepository questionRepository, OptionRepository optionRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }


    public Answer submitAnswer( AnswerRequest answerRequest, String username) throws AccessDeniedException {
        Question question = questionRepository.findById(answerRequest.getQuestionId()).orElseThrow(()->new EntityNotFoundException("Question not found"));

        User user = new User();

        if (username != null){
            user = userRepository.findByUsername(username).orElseThrow(()->new EntityNotFoundException("User not found"));
        }

        if (user != null && answerRepository.findByRespondentAndQuestion(user,question).isPresent()){
            throw new AccessDeniedException("You already answered to this question");
        }

        Answer answer = Answer.builder()
                .question(question)
                .respondent(user)
                .timestamp(Instant.now().toEpochMilli())
                .build();


        switch (question.getQuestionType()){
            case TEXT -> {
                answer.setTextAnswer(answerRequest.getTextAnswer());
            }
            case SINGLE_CHOICE, MULTIPLE_CHOICE -> {
                if (answerRequest.getSelectedOptionId() == null || answerRequest.getSelectedOptionId().isEmpty()){
                    throw new IllegalArgumentException("Options must be provided");
                }
                List<Option> selectedOptions = optionRepository.findAllById(answerRequest.getSelectedOptionId());
                answer.setSelectedOptions(selectedOptions);
            }
        }
        return answerRepository.save(answer);
    }

    public List<Answer> submitAnswers(List<AnswerRequest> answerRequests, String username) throws AccessDeniedException {
        List<Answer> answers = new ArrayList<>();

        for (AnswerRequest answerRequest : answerRequests){
            answers.add(submitAnswer(answerRequest,username));
        }
        return answers;
    }

    public boolean hasUserSubmitted(Long surveyId, String username) {
        Question question = questionRepository.findBySurveyId(surveyId).get(0);
        if (username != null) {
            return answerRepository.existsByQuestionIdAndRespondent_Username(question.getId(), username);
        } else {
            return false;
        }
    }

}
