package com.example.calculator.rpn;

import java.util.Arrays;

public enum Operator {
    OPENING_PARENTHESIS(5, '('), CLOSING_PARENTHESIS(5, ')'), MUL(4, '*'),
    DIV(4, '/'), ADD(3, '+'), SUB(3, '-');

    private final int precedence;
    private final char sign;

    Operator(int precedence, char sign) {
        this.precedence = precedence;
        this.sign = sign;
    }

    public int getPrecedence() {
        return precedence;
    }

    public char getSign() {
        return sign;
    }

    public static boolean isOperator(char character) {
        return Arrays.stream(Operator.values())
                .anyMatch(o -> o.getSign() == character);
    }

    public static Operator getOperatorFromChar(char character) {
        return Arrays.stream(Operator.values())
                .filter(o -> o.getSign() == character)
                .findFirst()
                .orElse(null);
    }
}
