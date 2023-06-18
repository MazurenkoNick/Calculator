package com.example.calculator.controller;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.service.MathExpressionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expression")
@AllArgsConstructor
public class MathExpressionController {

    private final MathExpressionService expressionService;

    @PostMapping
    public ResponseEntity<MathExpression> saveMathExpression(@Valid MathExpression expression, HttpServletRequest request) {
        MathExpression result = expressionService.save(expression, request.getRemoteAddr());

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}