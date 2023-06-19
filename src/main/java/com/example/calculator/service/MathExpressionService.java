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

    /**
     * If formatted expression equals to the persisted expression in the database,
     * the persisted one will be retrieved and new expression will not be persisted again.
     * Otherwise, it'll be saved.
     * @param expression default mathematical expression
     * @return {@link MathExpression}
     */
    @Transactional
    public MathExpression save(MathExpression expression) {
        String formattedExpression = expression.getFormattedExpression();
        MathExpression samePersistedExpression = expressionRepository.findByExpression(formattedExpression);

        // if the same expression exists, return it
        if (samePersistedExpression != null) {
            return samePersistedExpression;
        }

        // if expression is new, persist it and return persisted entity
        expression.setExpression(formattedExpression);
        return expressionRepository.save(expression);
    }

    @Transactional
    public MathExpression calculateAndSave(MathExpression expression) {
        String formattedExpression = expression.getFormattedExpression();
        MathExpression samePersistedExpression = expressionRepository.findByExpression(formattedExpression);

        // if expression is new, calculate, persist it and return persisted entity
        BigDecimal answer = calculatorService.calculate(expression);
        double doubleAnswer = answer.doubleValue();

        // if the same expression exists, add new answer and return it
        if (samePersistedExpression != null) {
            samePersistedExpression.getAnswers().add(doubleAnswer); // add new calculated value
            return samePersistedExpression;
        }

        expression.setExpression(formattedExpression);
        expression.getAnswers().add(doubleAnswer);
        return expressionRepository.save(expression);
    }

    @Transactional
    public MathExpression findByExpression(String expression) {
        return expressionRepository.findByExpression(expression);
    }

    @Transactional
    public MathExpression resolveEquationAndSave(MathExpression equation) {
        for (Double root : equation.getAnswers()) {
            boolean isRoot = calculatorService.checkRootOfEquation(root, equation);
            if (!isRoot) {
                throw new IllegalArgumentException("Root does not match the real answer");
            }
        }
        return save(equation);
    }
}
