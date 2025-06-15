package com.revenuepulse.sales.controller;

import com.revenuepulse.sales.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle specific ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse error = new ErrorResponse("ERR_" + ex.getStatusCode().value(), ex.getReason());
        return Mono.just(ResponseEntity.status(ex.getStatusCode()).body(error));
    }

    // Handle bad input
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse("ERR_400", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    // Catch-all fallback
    @ExceptionHandler(Throwable.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAllUnhandledExceptions(Throwable ex) {
        ErrorResponse error = new ErrorResponse("ERR_500", "Internal server error occurred.");
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }
}


