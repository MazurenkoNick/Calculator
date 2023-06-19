package com.example.calculator.controller;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.service.MathExpressionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expression")
@AllArgsConstructor
public class MathExpressionController {

    private final MathExpressionService expressionService;

    /**
     * Method is used to save the mathematical expression without calculating it.
     * It retrieves {@link MathExpression#getExpression()} and saves it by it.
     *
     * @param expression default mathematical expression
     * @return {@link ResponseEntity<MathExpression>} validated and persisted mathematical expression
     */
    @PostMapping
    public ResponseEntity<MathExpression> saveMathExpression(@Valid @RequestBody MathExpression expression) {
        MathExpression persistedExpression = expressionService.save(expression);

        return new ResponseEntity<>(persistedExpression, HttpStatus.CREATED);
    }

    /**
     * Method is used to save the mathematical expression after calculating it.
     * It retrieves {@link MathExpression#getExpression()} and saves it by it.
     *
     * @param expression default mathematical expression
     * @return {@link ResponseEntity<MathExpression>} validated and persisted mathematical expression
     */
    @PostMapping("/calculate")
    public ResponseEntity<MathExpression> calculateAndSave(@Valid @RequestBody MathExpression expression) {
        MathExpression persistedExpression = expressionService.calculateAndSave(expression);

        return new ResponseEntity<>(persistedExpression, HttpStatus.CREATED);
    }

    /**
     * @param equation default mathematical equation with unknown value 'x'.
     * It retrieves equation from {@link MathExpression#getExpression()} field and all x's from {@link MathExpression#getAnswers()}}.
     * If we replace 'x' in equation with values retrieved from {@link MathExpression#getAnswers()}} and both sides are equal,
     * the equation will be saved to the database.
     *
     * @return {@link ResponseEntity<MathExpression>} calculated and persisted mathematical equation
     */
    @PostMapping("/checkEquation")
    public ResponseEntity<MathExpression> checkEquationAndSave(@Valid @RequestBody MathExpression equation) {
        MathExpression persistedEquation = expressionService.checkRootsOfEquationAndSave(equation);
        return new ResponseEntity<>(persistedEquation, HttpStatus.CREATED);
    }

    /**
     * Searches for {@link MathExpression} from the database using expression, which is passed as an argument.
     * If expression is not found, null will be returned.
     *
     * @param expression default mathematical expression
     * @return {@link ResponseEntity<MathExpression>}
     */
    @GetMapping("/searchByExpression")
    public ResponseEntity<MathExpression> getMathExpression(@RequestBody String expression) {
        MathExpression persistedExpression = expressionService.findByExpression(expression);
        return ResponseEntity.ok(persistedExpression);
    }

    @GetMapping("/searchByAnswers")
    public ResponseEntity<List<MathExpression>> getMathExpressions(@RequestBody List<Double> answers) {
        List<MathExpression> expressions = expressionService.findByAnswers(answers);
        return ResponseEntity.ok(expressions);
    }
}
