package com.example.calculator.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class ErrorMessage {

    private HttpStatus status;
    private LocalDateTime date;
    private String message;
    private String description;

    public void addDescription(String text) {
        if (description != null) {
            description += text + "; ";
        }
        else {
            description = text + "; ";
        }
    }
}
