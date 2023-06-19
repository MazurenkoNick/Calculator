package com.example.calculator.service;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.exception.InvalidExpressionException;
import com.example.calculator.repository.MathExpressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MathExpressionService {

    private final CalculatorService calculatorService;
    private final MathExpressionRepository expressionRepository;

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

        return persist(expression);
    }

    @Transactional
    public MathExpression calculateAndSave(MathExpression expression) {
        if (expression.isEquation()) {
            throw new InvalidExpressionException("Equation can't be calculated");
        }

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

        expression.getAnswers().add(doubleAnswer);
        expression.setExpression(formattedExpression);

        return persist(expression);
    }

    @Transactional
    public MathExpression checkRootsOfEquationAndSave(MathExpression equation) {
        for (Double root : equation.getAnswers()) {
            boolean isRoot = calculatorService.checkRootOfEquation(root, equation);
            if (!isRoot) {
                throw new IllegalArgumentException("Root does not match the real answer");
            }
        }

        return persist(equation);
    }

    @Transactional
    public MathExpression findByExpression(String expression) {
        return expressionRepository.findByExpression(expression);
    }

    @Transactional
    public List<MathExpression> findByAnswers(List<Double> answers) {
        return expressionRepository.findByAnswers(answers); // знайти всі рівняння, що мають один із зазначених коренів
    }

    private MathExpression persist(MathExpression expression) {
        if (expression.isEquation()) {
            expression.setIsEquation(true);
        }

        return expressionRepository.save(expression);
    }
}
