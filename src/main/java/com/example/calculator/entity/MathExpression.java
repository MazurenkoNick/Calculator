package com.example.calculator.entity;

import com.example.calculator.validation.MathematicalExpression;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expressions")
@NoArgsConstructor
@Getter @Setter @ToString
public class MathExpression {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "expression", nullable = false)
    @MathematicalExpression
    private String expression;

    @Column(name = "user_ip")
    private String userIp;
}
