package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.Answer;
import com.example.surveyapp.Entity.Question;
import com.example.surveyapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findByQuestion(Question question);

    Optional<Answer> findByRespondentAndQuestion(User respondent, Question question);

    List<Answer> findByRespondent(User respondent);

    List<Answer> findByQuestion_Survey_Id(Long id);

    boolean existsByQuestionIdAndRespondent_Username(Long questionId, String username);

}
