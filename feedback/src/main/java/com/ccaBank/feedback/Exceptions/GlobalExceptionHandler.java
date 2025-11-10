package com.ccaBank.feedback.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NosuchExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ProblemDetail handleNoSuchExistException(NosuchExistException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Ressource introuvable");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

}
