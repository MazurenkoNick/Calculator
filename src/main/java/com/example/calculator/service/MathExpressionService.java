package com.example.calculator.service;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.repository.MathExpressionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MathExpressionService {

    private final CalculatorService calculatorService;
    private final MathExpressionRepository expressionRepository;
    private final HttpServletRequest request;

    public MathExpression save(MathExpression expression) {
        return expressionRepository.save(expression);
    }

    @Transactional
    public MathExpression calculateAndSave(MathExpression expression) {
        String formattedExpression = expression.getFormattedExpression();
        MathExpression samePersistedExpression = expressionRepository.findByExpression(formattedExpression);

        // if the same expression exists, return it
        if (samePersistedExpression != null) {
            return samePersistedExpression;
        }

        // if expression is new, calculate, persist it and return persisted entity
        BigDecimal answer = calculatorService.calculate(expression);
        double doubleAnswer = answer.doubleValue();

        expression.setExpression(formattedExpression);
        expression.getAnswers().add(doubleAnswer);
        return expressionRepository.save(expression);
    }
}
