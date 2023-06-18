package com.example.calculator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayDeque;
import java.util.regex.Pattern;

public class MathExpressionValidator implements ConstraintValidator<MathematicalExpression, String> {

    private static final Pattern invalidOperandOrderPattern = Pattern.compile("\\d[+*/-][+*/]\\d");

    @Override
    public boolean isValid(String expression, ConstraintValidatorContext context) {
        boolean balancedParentheses = validParenthesesSymmetry(expression);
        boolean isOperatorOrderValid = validOperatorOrder(expression);
        return balancedParentheses && isOperatorOrderValid;
    }

    private boolean validParenthesesSymmetry(String expression) {
        ArrayDeque<Character> stack = new ArrayDeque<>();

        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                stack.push(ch);
            }
            else if (ch == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
        }

        return stack.isEmpty();
    }

    private boolean validOperatorOrder(String expression) {
        return !invalidOperandOrderPattern.matcher(expression).find();
    }
}
