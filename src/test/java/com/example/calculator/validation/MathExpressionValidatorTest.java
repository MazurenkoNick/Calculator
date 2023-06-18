package com.example.calculator.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MathExpressionValidatorTest {

    private static MathExpressionValidator mathExpressionValidator;

    @BeforeAll
    static void setUpBeforeAll() {
        mathExpressionValidator = new MathExpressionValidator();
    }

    @Test
    void testParenthesesSymmetryTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = MathExpressionValidator.class.getDeclaredMethod("validParenthesesSymmetry", String.class);
        method.setAccessible(true);

        String validExpression1 = "(31+2*(1+5*(6+2)))";
        String validExpression2 = "((31+2*(1+5*(6+2))))";
        String validExpression3 = "(2+2)(3+3)((4+4))";
        String validExpression4 = "2+2";
        String invalidExpression1 = "(";
        String invalidExpression2 = "(((2+1))";
        String invalidExpression3 = "(2+1))";
        String invalidExpression4 = "((2+1))((1+2)";
        String invalidExpression5 = ")1+2(";

        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression1));
        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression2));
        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression3));
        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression4));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression1));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression2));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression3));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression4));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression5));
    }

    @Test
    void testValidOperatorOrder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = MathExpressionValidator.class.getDeclaredMethod("validOperatorOrder", String.class);
        method.setAccessible(true);


        String validExpression1 = "1+-5";
        String validExpression2 = "1*-5";
        String validExpression3 = "1/-5";
        String validExpression4 = "1-5";
        String validExpression5 = "1+5";
        String validExpression6 = "((31+2*(1+5*(6+2))))";
        String invalidExpression1 = "1-+5";
        String invalidExpression2 = "1++5";
        String invalidExpression3 = "1/+5";
        String invalidExpression4 = "1*+5";
        String invalidExpression5 = "1+/5";
        String invalidExpression6 = "1-/5";
        String invalidExpression7 = "1*/5";
        String invalidExpression8 = "1//5";

        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression1));
        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression2));
        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression3));
        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression4));
        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression5));
        assertTrue((Boolean) method.invoke(mathExpressionValidator, validExpression6));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression1));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression2));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression3));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression4));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression5));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression6));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression7));
        assertFalse((Boolean) method.invoke(mathExpressionValidator, invalidExpression8));
    }
}
