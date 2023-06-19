package com.example.calculator.rpn;

import com.example.calculator.entity.MathExpression;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class ReversePolishNotation {

    private final StringBuilder rpnExpression;
    private final ArrayDeque<Operator> stack;
    private static final Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");
    private static final Pattern negativeNumberPattern = Pattern.compile("-\\d+(\\.\\d+)?");

    public ReversePolishNotation() {
        this.rpnExpression = new StringBuilder();
        this.stack = new ArrayDeque<>();
    }

    /**
     * parameter {@link MathExpression} will be formatted to be parsed more easily to the RPN.
     * The method will iterate over every character of the formatted expression and convert it to the RPN.
     * @param mathExpression is used during conversion to rpn
     * @return reverse polish notation from mathematical expression
     */
    public String convertToRpn(MathExpression mathExpression) {
        String formattedExpression = mathExpression.getFormattedExpression();
        Matcher numberMatcher = numberPattern.matcher(formattedExpression);
        Matcher negativeNumberMatcher = negativeNumberPattern.matcher(formattedExpression);
        boolean isPrevCharOperator = false;

        // iterates over every character of the string, so converts it to the rpn
        for (int i = 0; i < formattedExpression.length(); i++) {
            char character = formattedExpression.charAt(i);

            // e.g. +- *- /- -- or string starts with - (e.g. "-6.99 + ...")
            if ((i == 0 || isPrevCharOperator)
                    && Operator.SUB.equals(Operator.getOperatorFromChar(character))) {
                // -5.53 ; -2
                if (negativeNumberMatcher.find()) {
                    String number = negativeNumberMatcher.group(); // the number which was found by negativeNumberMatcher
                    i = negativeNumberMatcher.end() - 1; // the last index of number (+ 1 in the next iteration)
                    appendNumber(number);
                }
                isPrevCharOperator = false;
            }
            // e.g. +-*/
            else if (Operator.isOperator(character)) {
                appendOperator(Operator.getOperatorFromChar(character));
                isPrevCharOperator = true;
            }
            // e.g. 5.53 ; 2 ;
            else if (numberMatcher.find(i)) { // find number
                String number = numberMatcher.group(); // the number which was found by numberMatcher
                i = numberMatcher.end() - 1; // the last index of number (+ 1 in the next iteration)
                isPrevCharOperator = false;
                appendNumber(number);
            }
        }
        appendStackToRpn();

        return getRpnExpression();
    }

    /**
     * Appends number to the RPN
     * @param number
     */
    private void appendNumber(String number) {
        rpnExpression.append(number).append(" ");
    }

    /**
     * If the operator from the argument is equal to closing parenthesis,
     * it means that the method will retrieve everything between the opening and
     * closing parentheses from the {@link ReversePolishNotation#stack} and append it to the RPN.
     * Otherwise, the method will push the operator to the {@link ReversePolishNotation#stack}.
     * Before that, the method will be popping the operators from the stack and adding them to the RPN
     * unless it's an opening parenthesis or the precedence of this current operator is lower than the one from the argument.
     * @param operator
     */
    private void appendOperator(Operator operator) {
        if (operator.equals(Operator.CLOSING_PARENTHESIS)) {
            appendBetweenParenthesesToRpn();
        }
        else {
            while (!stack.isEmpty() &&
                    !stack.peek().equals(Operator.OPENING_PARENTHESIS) &&
                    operator.getPrecedence() <= stack.peek().getPrecedence()) {
                popAndAppendToRpn();
            }
            stack.push(operator);
        }
    }

    /**
     * The method will add every operator to the RPN unless it hits an opening parenthesis
     */
    private void appendBetweenParenthesesToRpn() {
        while (!Objects.equals(stack.peek(), Operator.OPENING_PARENTHESIS)) {
            popAndAppendToRpn();
        }
        stack.pop(); // pop opening parenthesis but not add it to rpn
    }

    private void appendStackToRpn() {
        while (!stack.isEmpty()) {
            popAndAppendToRpn();
        }
    }

    private void popAndAppendToRpn() {
        var operator = stack.pop();
        rpnExpression
                .append(operator.getSign())
                .append(" ");
    }

    private String getRpnExpression() {
        int lastIdx = rpnExpression.length() - 1;
        rpnExpression.deleteCharAt(lastIdx); // remove last " "

        String res = rpnExpression.toString();
        // clean up
        stack.clear();
        rpnExpression.setLength(0);
        return res.strip();
    }
}
