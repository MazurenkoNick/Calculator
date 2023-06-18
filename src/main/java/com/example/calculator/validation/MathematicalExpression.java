package com.example.calculator.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MathExpressionValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MathematicalExpression {

    String message() default "Invalid mathematical expression";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
