package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.Option;
import com.example.surveyapp.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option,Long> {

    List<Option> findByQuestionIdOrderByPositionAsc(Long questionId);
}
