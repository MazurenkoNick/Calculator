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

    public ReversePolishNotation() {
        this.rpnExpression = new StringBuilder();
        this.stack = new ArrayDeque<>();
    }

    public String convertToRpn(MathExpression mathExpression) {
        String formattedExpression = mathExpression.getFormattedExpression();
        Matcher matcher = numberPattern.matcher(formattedExpression);

        // iterates over every character of the string, so converts it to the rpn
        for (int i = 0; i < formattedExpression.length(); i++) {
            char character = formattedExpression.charAt(i);

            if (Operator.isOperator(character)) {
                appendOperator(Operator.getOperatorFromChar(character));
                // todo: check and resolve if there's another operator after the match (e.g. 4+-5)
            }
            else if (matcher.find(i)) { // find number
                String number = matcher.group(); // the number which was found by matcher
                i = matcher.end() - 1; // the last index of number (+ 1 in the next iteration)
                appendNumber(number);
            }
        }
        appendStackToRpn();

        return getRpnExpression();
    }

    private void appendNumber(String number) {
        rpnExpression.append(number).append(" ");
    }

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

    private void appendBetweenParenthesesToRpn() {
        while (!Objects.equals(stack.peek(), Operator.OPENING_PARENTHESIS)) {
            popAndAppendToRpn();
        }
        stack.pop(); // pop opening parenthesis
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
        rpnExpression.setLength(0);
        return res.strip();
    }
}
