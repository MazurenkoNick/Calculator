package com.example.calculator.service;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.exception.InvalidExpressionException;
import com.example.calculator.rpn.Operator;
import com.example.calculator.rpn.ReversePolishNotation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.function.BinaryOperator;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final ReversePolishNotation reversePolishNotation;
    private final Map<Operator, BinaryOperator<BigDecimal>> operations = Map.of(
            Operator.DIV, BigDecimal::divide,
            Operator.MUL, BigDecimal::multiply,
            Operator.SUB, BigDecimal::subtract,
            Operator.ADD, BigDecimal::add
    );

    /**
     * @param root is used to be inserted in the equation
     * @param equation
     * @return true if the left and right side has maximum difference 10^-9, false otherwise.
     */
    public boolean checkRootOfEquation(Double root, MathExpression equation) {
        String textEquation = equation.getFormattedExpression().replaceAll("x", root.toString());
        String[] sidesOfEquation = textEquation.split("=");
        MathExpression leftSide = new MathExpression();
        MathExpression rightSide = new MathExpression();

        if (sidesOfEquation.length < 2) {
            throw new InvalidExpressionException("Equation does not have an equal sign");
        }

        leftSide.setExpression(sidesOfEquation[0]);
        rightSide.setExpression(sidesOfEquation[1]);

        BigDecimal leftSideResult = calculate(leftSide);
        BigDecimal rightSideResult = calculate(rightSide);

        return leftSideResult.subtract(rightSideResult).abs().doubleValue() <= 0.1E-9;
    }

    /**
     * Method will convert mathematical expression to the RPN.
     * It'll calculate the RPN using internal methods.
     *
     * @param expression default mathematical expression
     * @return {@link BigDecimal} result of the calculation
     */
    public BigDecimal calculate(MathExpression expression) {
        String rpnExpression = reversePolishNotation.convertToRpn(expression);
        return calculateRpnExpression(rpnExpression);
    }

    /**
     * The method takes RNP as an argument. It'll split it by delimiter (" " by default).
     * It'll iterate over every value in the split expression. If the current value is an operator,
     * two numbers from stack will be popped, and they will be operated using the current operator.
     * The result of this calculation will be pushed back to the stack.
     * If the current value is a number, it'll be pushed to the stack.
     * After iterations, the stack must be empty, because every number from the expression
     * must be operated with one of the operators. If it's not, {@link InvalidExpressionException} will be thrown.
     *
     * @param expression Reverse Polish Notation
     * @return {@link BigDecimal} result of the calculation
     */
    private BigDecimal calculateRpnExpression(String expression) {
        if (expression.isEmpty())
            return BigDecimal.ZERO;

        String[] splitExpression = expression.split(" ");
        ArrayDeque<BigDecimal> stack = new ArrayDeque<>();

        for (String s : splitExpression) {
            // if current s is an operator - pop two numbers from the stack and apply the operation
            if (s.length() == 1 && Operator.isOperator(s.charAt(0))) {
                Operator operator = Operator.getOperatorFromChar(s.charAt(0));
                calculateFromTopStack(operator, stack);
            }
            else {
                stack.push(new BigDecimal(s));
            }
        }

        BigDecimal result = stack.pop();
        if (!stack.isEmpty()) {
            throw new InvalidExpressionException();
        }
        return result;
    }

    /**
     * @param operator is used to operate over two values from top of the stack
     * @param stack is used to retrieve two values from the top of itself
     */
    private void calculateFromTopStack(Operator operator, ArrayDeque<BigDecimal> stack) {
        BigDecimal n2 = stack.pop();
        BigDecimal n1 = stack.pop();
        stack.push(operate(operator, n1, n2));
    }

    private BigDecimal operate(Operator operator, BigDecimal n1, BigDecimal n2) {
        return operations.get(operator).apply(n1, n2);
    }
}
