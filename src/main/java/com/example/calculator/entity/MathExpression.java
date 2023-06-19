package com.example.calculator.entity;

import com.example.calculator.validation.MathematicalExpression;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(
            name = "results",
            joinColumns = @JoinColumn(name = "expression_id")
    )
    @Column(name = "value")
    private Set<Double> answers = new HashSet<>();

    @Column(name = "IsEquation")
    private boolean IsEquation; // false by default

    /**
     * Method returns correctly formatted mathematical expression without
     * unnecessary spaces. Method replaces all commas with dots as well.
     *
     * @return new string is created after formation of the {@link MathExpression#expression}
     */
    @JsonIgnore
    public String getFormattedExpression() {
        StringBuilder sb = new StringBuilder(expression);
        int length = sb.length();

        for (int i = 0; i < length; i++) {
            char c = sb.charAt(i);

            if (Character.isWhitespace(c)) {
                sb.deleteCharAt(i);
                length--;
                i--;
            }
            else if (c == ',') {
                sb.setCharAt(i, '.');
            }
        }

        return sb.toString();
    }

    public boolean isEquation() {
        return expression.split("=").length == 2;
    }
}
