package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.*;
import com.example.surveyapp.Repository.AnswerRepository;
import com.example.surveyapp.Repository.QuestionRepository;
import com.example.surveyapp.Repository.SurveyRepository;
import com.example.surveyapp.dto.OptionStats;
import com.example.surveyapp.dto.QuestionResultResponse;
import com.example.surveyapp.dto.SurveyResultsResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public AnalyticsService(SurveyRepository surveyRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public SurveyResultsResponse getResultsForSurvey(Long surveyId,String username) throws AccessDeniedException {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(()-> new EntityNotFoundException("Survey not found"));

        if (!survey.getCreator().getUsername().equals(username)){
            throw new AccessDeniedException("Not allowed to view analytics of this survey");
        }

        List<Question> questions = questionRepository.findBySurveyIdOrderByPositionAsc(surveyId);
        List<Answer> allAnswers = answerRepository.findByQuestion_Survey_Id(surveyId);

        SurveyResultsResponse response = new SurveyResultsResponse();
        response.setSurveyId(survey.getId());
        response.setTitle(survey.getTitle());

        List<QuestionResultResponse> questionResultResponses = new ArrayList<>();
        for (Question question: questions){
            QuestionResultResponse questionResultResponse = new QuestionResultResponse();
            questionResultResponse.setQuestionId(question.getId());
            questionResultResponse.setText(questionResultResponse.getText());
            questionResultResponse.setQuestionType(question.getQuestionType());

            List<Answer> questionAnswers = allAnswers.stream()
                    .filter(ans -> ans.getQuestion().getId().equals(question.getId()))
                    .toList();

            if (question.getQuestionType() == QuestionType.TEXT) {
                List<String> texts = questionAnswers.stream()
                        .map(Answer::getTextAnswer)
                        .filter(Objects::nonNull)
                        .toList();
                questionResultResponse.setTextAnswers(texts);

            } else {
                Map<Option, Long> optionCounts = questionAnswers.stream()
                        .flatMap(ans -> ans.getSelectedOptions().stream())
                        .collect(Collectors.groupingBy(opt -> opt, Collectors.counting()));

                List<OptionStats> stats = optionCounts.entrySet().stream()
                        .map(e -> {
                            OptionStats os = new OptionStats();
                            os.setOptionId(e.getKey().getId());
                            os.setText(e.getKey().getText());
                            os.setCount(e.getValue());
                            return os;
                        })
                        .toList();

                questionResultResponse.setOptionStats(stats);
            }

            questionResultResponses.add(questionResultResponse);
        }

        response.setQuestionResultResponses(questionResultResponses);
        return response;
    }
}
