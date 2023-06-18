package com.example.calculator.service;

import com.example.calculator.entity.MathExpression;
import com.example.calculator.repository.MathExpressionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MathExpressionService {

    private final MathExpressionRepository expressionRepository;
    private final HttpServletRequest request;

    public MathExpression save(MathExpression expression) {
        expression.setUserIp(retrieveClientIp(request));
        return expressionRepository.save(expression);
    }

    private String retrieveClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");

        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
