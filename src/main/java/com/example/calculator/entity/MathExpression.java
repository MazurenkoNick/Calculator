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

    /**
     * Method returns correctly formatted mathematical expression without
     * unnecessary spaces. Method replaces all commas with dots as well.
     *
     * @return new string is created after formation of the {@link MathExpression#expression}
     */
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
}
