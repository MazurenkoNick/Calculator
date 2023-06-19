package com.example.calculator.repository;

import com.example.calculator.entity.MathExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MathExpressionRepository extends JpaRepository<MathExpression, Long> {

    @Query("SELECT e FROM MathExpression e LEFT JOIN FETCH e.answers WHERE e.expression = :expression")
    MathExpression findByExpression(@Param("expression") String expression);

    @Query("SELECT DISTINCT e FROM MathExpression e LEFT JOIN FETCH e.answers a WHERE a IN :answers")
    List<MathExpression> findByAnswers(@Param("answers") List<Double> answers);
}
