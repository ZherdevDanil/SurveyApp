package com.example.surveyapp.Repository;

import com.example.surveyapp.Entity.Option;
import com.example.surveyapp.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OptionRepository extends JpaRepository<Option,Long> {

    List<Option> findByQuestionIdOrderByPositionAsc(Long questionId);
}
