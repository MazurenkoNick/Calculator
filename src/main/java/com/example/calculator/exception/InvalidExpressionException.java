package com.example.calculator.exception;

public class InvalidExpressionException extends RuntimeException {

    public InvalidExpressionException() {
        super("Inval");
    }

    public InvalidExpressionException(String message) {
        super(message);
    }
}
