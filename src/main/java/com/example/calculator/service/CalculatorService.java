package com.example.calculator.service;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.rpn.ReversePolishNotation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CalculatorService {

    private final ReversePolishNotation reversePolishNotation;

    public String calculate(MathExpression expression) {
        String rpnExpression = reversePolishNotation.convertToRpn(expression);
        return calculate(rpnExpression);
    }

    private String calculate(String rpnExpression) {
        return null; // todo: make calculation process using rpn
    }
}
