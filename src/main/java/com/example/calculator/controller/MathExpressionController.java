package com.example.calculator.controller;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.service.MathExpressionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expression")
@AllArgsConstructor
public class MathExpressionController {

    private final MathExpressionService expressionService;

    /**
     * Method is used to retrieve
     *
     * @param expression defaul mathematical expression
     * @return {@link ResponseEntity<MathExpression>} validated and persisted mathematical expression
     */
    @PostMapping
    public ResponseEntity<MathExpression> saveMathExpression(@Valid @RequestBody MathExpression expression) {
        MathExpression persistedExpression = expressionService.save(expression);

        return new ResponseEntity<>(persistedExpression, HttpStatus.CREATED);
    }

    @PostMapping("/calculate")
    public ResponseEntity<MathExpression> calculateAndSave(@Valid @RequestBody MathExpression expression) {
        MathExpression persistedExpression = expressionService.calculateAndSave(expression);

        return new ResponseEntity<>(persistedExpression, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<MathExpression> getMathExpression(@RequestBody String expression) {
        MathExpression persistedExpression = expressionService.findByExpression(expression);
        return ResponseEntity.ok(persistedExpression);
    }
}
