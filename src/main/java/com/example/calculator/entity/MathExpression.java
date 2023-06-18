package com.example.calculator.entity;

import com.example.calculator.validation.MathematicalExpression;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    @NotEmpty
    @MathematicalExpression
    private String expression;

    @Column(name = "user_ip")
    private String userIp;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
