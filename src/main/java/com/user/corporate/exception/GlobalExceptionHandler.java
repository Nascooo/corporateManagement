package com.user.corporate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    private String message;

    public GlobalExceptionHandler(String message) {
        super(message);
        this.message = message;
    }

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler(value = GlobalExceptionHandler.class)
    public ResponseEntity GlobalExceptionHandler(GlobalExceptionHandler exception) {
        GeneralErrorDTO generalErrorDTO = new GeneralErrorDTO();
        generalErrorDTO.setMessage(exception.getMessage());
        generalErrorDTO.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity(generalErrorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
