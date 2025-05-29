package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.Option;
import com.example.surveyapp.Entity.Question;
import com.example.surveyapp.Entity.QuestionType;
import com.example.surveyapp.Entity.Survey;
import com.example.surveyapp.Repository.OptionRepository;
import com.example.surveyapp.Repository.QuestionRepository;
import com.example.surveyapp.Repository.SurveyRepository;
import com.example.surveyapp.dto.CreateOptionRequest;
import com.example.surveyapp.dto.CreateQuestionRequest;
import com.example.surveyapp.dto.QuestionUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    public final QuestionRepository questionRepository;
    public final OptionRepository optionRepository;
    public final SurveyRepository surveyRepository;

    private final UserService userService;

    public QuestionService(QuestionRepository questionRepository, OptionRepository optionRepository, SurveyRepository surveyRepository, UserService userService) {
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.surveyRepository = surveyRepository;
        this.userService = userService;
    }

    public Question addQuestionToSurvey(Long surveyId, Question question){
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(()->new RuntimeException("Survey not found"));
        question.setSurvey(survey);
        return questionRepository.save(question);
    }


    public Question createQuestion(Long surveyId, QuestionUpdateDto request, String username) throws AccessDeniedException {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found"));

        if (!survey.getCreator().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not owner of this survey");
        }

        Question question = Question.builder()
                .survey(survey)
                .text(request.getText())
                .questionType(request.getType())
                .position(request.getPosition())
                .build();

        Question savedQuestion = questionRepository.save(question);

        if (request.getType() == QuestionType.SINGLE_CHOICE || request.getType() == QuestionType.MULTIPLE_CHOICE) {
            List<Option> options = request.getOptions().stream()
                    .map(o -> Option.builder()
                            .question(savedQuestion)
                            .text(o.getText())
                            .position(o.getPosition())
                            .build())
                    .toList();

            optionRepository.saveAll(options);
            savedQuestion.setOptions(options);
        }

        return savedQuestion;
    }



    public List<Question> getQuestionsBySurvey(Long surveyId,String username) throws AccessDeniedException {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(()->new EntityNotFoundException("Survey not found"));


        if (survey.isRequireAuth()){
            if (!survey.getCreator().getUsername().equals(username)){
                throw new AccessDeniedException("You are not allowed to view questions");
            }
        }
        return questionRepository.findBySurveyIdOrderByPositionAsc(surveyId);
    }


    public void deleteQuestion(Long questionId,String username) throws AccessDeniedException {
        Question question = questionRepository.findById(questionId).orElseThrow(()->new EntityNotFoundException("Question not found"));

        if (!question.getSurvey().getCreator().getUsername().equals(username)){
            throw new AccessDeniedException("You are not allowed to delete this question");
        }
        questionRepository.delete(question);
    }

    @Transactional
    public Question updateQuestion(Long questionId, QuestionUpdateDto questionUpdateDto, String username) throws AccessDeniedException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        if (!question.getSurvey().getCreator().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not allowed to change question");
        }
        question.setText(questionUpdateDto.getText());
        question.setQuestionType(questionUpdateDto.getType());
        question.setPosition(questionUpdateDto.getPosition());

        if (questionUpdateDto.getType() == QuestionType.SINGLE_CHOICE || questionUpdateDto.getType() == QuestionType.MULTIPLE_CHOICE) {
            question.getOptions().clear();

            int index = 0;
            for ( CreateOptionRequest createOptionRequest : questionUpdateDto.getOptions()) {
                Option option = Option.builder()
                        .question(question)
                        .text(createOptionRequest.getText())
                        .position(index++)
                        .build();
                question.getOptions().add(option);
            }

        } else {

            question.getOptions().clear();
        }

        return questionRepository.save(question);
    }


}




