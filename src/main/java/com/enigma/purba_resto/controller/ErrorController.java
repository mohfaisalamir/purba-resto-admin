package com.enigma.purba_resto.controller;

import com.enigma.purba_resto.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> responseStatusException (ResponseStatusException e) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(e.getStatusCode().value())
                .message(e.getReason())
                .build();
        return ResponseEntity
                .status(e.getStatusCode())
                .body(response);
    }
}
