package com.example.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidExpressionException extends RuntimeException {

    public InvalidExpressionException() {
        super("Invalid expression from input");
    }

    public InvalidExpressionException(String message) {
        super(message);
    }
}
