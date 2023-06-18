package com.example.calculator.repository;

import com.example.calculator.entity.MathExpression;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MathExpressionRepository extends JpaRepository<MathExpression, Long> {
}
