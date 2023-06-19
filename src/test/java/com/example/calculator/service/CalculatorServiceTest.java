package com.example.calculator.service;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.rpn.ReversePolishNotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CalculatorServiceTest {

    private CalculatorService calculatorService;
    private ReversePolishNotation rpn;

    @BeforeEach
    void setUpBeforeEach() {
        this.rpn = new ReversePolishNotation();
        this.calculatorService = new CalculatorService(rpn);
    }

    @Test
    public void calculate() {
        MathExpression expression1 = new MathExpression();
        expression1.setExpression("(-6.999 * -3.75 - ((-4.3 - -5.5)) + -2,2 ) / -2");

        MathExpression expression2 = new MathExpression();
        expression2.setExpression("2+3*(40-23*(5+2))");

        Assertions.assertEquals(new BigDecimal("-11.423125"), calculatorService.calculate(expression1));
        Assertions.assertEquals(new BigDecimal("-361"), calculatorService.calculate(expression2));
    }
}
