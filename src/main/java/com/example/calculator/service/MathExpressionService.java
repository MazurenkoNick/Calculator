package com.example.calculator.service;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.repository.MathExpressionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MathExpressionService {

    private final MathExpressionRepository expressionRepository;

    public MathExpression save(MathExpression expression, String userRemoteAddr) {
        expression.setUserIp(userRemoteAddr);
        return expressionRepository.save(expression);
    }
}
