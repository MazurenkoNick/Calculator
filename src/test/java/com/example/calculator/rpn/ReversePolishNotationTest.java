package com.example.calculator.rpn;

import com.example.calculator.entity.MathExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReversePolishNotationTest {

    private ReversePolishNotation reversePolishNotation;

    @BeforeEach
    void setUpBeforeEach() {
        reversePolishNotation = new ReversePolishNotation();
    }

    @Test
    void testConvertToRpn() {
        MathExpression expression1 = new MathExpression();
        expression1.setExpression("(-6.999 * -3.75 - ((-4.3 - -5.5)) + -2,2 ) / -2");
        String actual1 = "-6.999 -3.75 * -4.3 -5.5 - - -2.2 + -2 /";

        MathExpression expression2 = new MathExpression();
        expression2.setExpression("2+3*(40-23*(5+2))");
        String actual2 = "2 3 40 23 5 2 + * - * +";

        assertEquals(actual1, reversePolishNotation.convertToRpn(expression1));
        assertEquals(actual2, reversePolishNotation.convertToRpn(expression2));
    }
}
