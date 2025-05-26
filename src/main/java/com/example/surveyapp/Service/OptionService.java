package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.Option;
import com.example.surveyapp.Entity.Question;
import com.example.surveyapp.Entity.QuestionType;
import com.example.surveyapp.Repository.OptionRepository;
import com.example.surveyapp.Repository.QuestionRepository;
import com.example.surveyapp.dto.CreateOptionRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;
    private final UserService userService;

    public OptionService(OptionRepository optionRepository, QuestionService questionService, QuestionRepository questionRepository, UserService userService) {
        this.optionRepository = optionRepository;
        this.questionRepository = questionRepository;
        this.userService = userService;
    }


    public Option addOption(Long questionId, CreateOptionRequest createOptionRequest){
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));


        if (question.getQuestionType() == QuestionType.TEXT) {
            throw new IllegalArgumentException("Cannot add options to text-type questions");
        }

        Option option = Option.builder()
                .question(question)
                .text(createOptionRequest.getText())
                .position(createOptionRequest.getPosition())
                .build();

        return optionRepository.save(option);
    }

    public List<Option> getOptionsForQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new EntityNotFoundException("Question not found");
        }

        return optionRepository.findByQuestionIdOrderByPositionAsc(questionId);
    }

    public Option updateOption(Long optionId, CreateOptionRequest createOptionRequest) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Option not found"));

        option.setText(createOptionRequest.getText());
        option.setPosition(createOptionRequest.getPosition());

        return optionRepository.save(option);
    }

    public void deleteOption(Long optionId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Option not found"));

        optionRepository.delete(option);
    }
}
