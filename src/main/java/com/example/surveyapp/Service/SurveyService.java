package com.example.surveyapp.Service;

import com.example.surveyapp.Entity.*;
import com.example.surveyapp.Repository.*;
import com.example.surveyapp.Security.JwtService;
import com.example.surveyapp.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    public final SurveyRepository surveyRepository;
    public final UserRepository userRepository;

    public final JwtService jwtService;

    private final QuestionRepository questionRepository;

    private final OptionRepository optionRepository;
    private final AnswerRepository answerRepository;

    public SurveyService(SurveyRepository surveyRepository, UserRepository userRepository, JwtService jwtService, QuestionRepository questionRepository, OptionRepository optionRepository, AnswerRepository answerRepository){
        this.surveyRepository=surveyRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.answerRepository = answerRepository;
    }

    public List<Survey> getSurveys(Long id){
        List<Survey> surveys = surveyRepository.findByCreatorId(id);
        return surveys;
    }

    public Survey createSurvey(CreateSurveyRequest createSurveyRequest, String username) {
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        System.out.println("ЧИ createSurveyRequest - public" + createSurveyRequest.isPublic());

        Survey survey = Survey.builder()
                .title(createSurveyRequest.getTitle())
                .description(createSurveyRequest.getDescription())
                .requireAuth(createSurveyRequest.isRequireAuth())
                .activeFrom(createSurveyRequest.getActiveFrom())
                .activeUntil(createSurveyRequest.getActiveUntil())
                .creator(creator)
                .isActive(true)
                .isPublic(createSurveyRequest.isPublic())
                .build();
        System.out.println("Чи saved survey - public"+survey.isPublic());
        survey = surveyRepository.save(survey);

        for (QuestionUpdateDto qRequest : createSurveyRequest.getQuestions()) {
            Question question = Question.builder()
                    .survey(survey)
                    .text(qRequest.getText())
                    .questionType(qRequest.getType())
                    .position(qRequest.getPosition())
                    .build();

            question = questionRepository.save(question);

            if (qRequest.getType() == QuestionType.SINGLE_CHOICE || qRequest.getType() == QuestionType.MULTIPLE_CHOICE) {
                List<Option> options = new ArrayList<>();
                for (CreateOptionRequest opt : qRequest.getOptions()) {
                    options.add(Option.builder()
                            .question(question)
                            .text(opt.getText())
                            .position(opt.getPosition())
                            .build());
                }
                optionRepository.saveAll(options);
                question.setOptions(options);
            }
        }

        return survey;
    }


    public List<PublicSurveysResponse> getAllPublicSurveys(){
        List<Survey> allSurveys = surveyRepository.findAll();
        List<PublicSurveysResponse> publicSurveys = new ArrayList<>();

        for (Survey survey : allSurveys){
            if (survey.isPublic()){
                PublicSurveysResponse publicSurveysResponse = PublicSurveysResponse.builder()
                        .id(survey.getId())
                        .title(survey.getTitle())
                        .description(survey.getDescription())
                        .build();
                publicSurveys.add(publicSurveysResponse);

            }
        }
        return publicSurveys;
    }

    public UpdateSurveyRequest getSurveyDetails(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found"));



        List<Question> questions = questionRepository.findBySurveyId(survey.getId());

        List<QuestionUpdateDto> questionDtos = questions.stream()
                .map(q -> {
                    List<CreateOptionRequest> optionDtos = q.getOptions().stream()
                            .map(opt -> CreateOptionRequest.builder()
                                    .id(opt.getId())
                                    .text(opt.getText())
                                    .position(opt.getPosition())
                                    .build())
                            .toList();

                    return QuestionUpdateDto.builder()
                            .id(q.getId())
                            .text(q.getText())
                            .type(q.getQuestionType())
                            .position(q.getPosition())
                            .canEdit(!answerRepository.existsByQuestionId(q.getId()))
                            .options(optionDtos)
                            .build();
                })
                .toList();

        return UpdateSurveyRequest.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .requireAuth(survey.isRequireAuth())
                .isActive(survey.isActive())
                .questions(questionDtos)
                .build();
    }

    @Transactional
    public Survey finishSurvey(Long surveyId,String username) throws AccessDeniedException {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(()->new EntityNotFoundException());
        if (!survey.getCreator().getUsername().equals(username)){
            throw new AccessDeniedException("You are not allowed");
        }
        survey.setActive(false);
        return surveyRepository.save(survey);
    }



    public Optional<Survey> findById(Long id){
        return surveyRepository.findById(id);
    }


    public List<Survey> findByUser(User user){
        List<Survey> surveys = surveyRepository.findByCreatorId(user.getId());
        return surveys;
    }

    public void deleteSurvey(Long id){
        surveyRepository.deleteById(id);
    }

    public Survey save(Survey survey){
        return surveyRepository.save(survey);
    }


    public String extractUsername(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return null;
        }
        String jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }
}
