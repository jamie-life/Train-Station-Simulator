package com.jamie.metro.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class TrainException extends RuntimeException {
    private HttpStatus status;
    private String message;
}
