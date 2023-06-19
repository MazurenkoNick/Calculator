package com.example.calculator.repository;

import com.example.calculator.entity.MathExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MathExpressionRepository extends JpaRepository<MathExpression, Long> {

    @Query("SELECT e FROM MathExpression e LEFT JOIN FETCH e.answers WHERE e.expression = :expression")
    MathExpression findByExpression(@Param("expression") String expression);
}
