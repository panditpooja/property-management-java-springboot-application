package com.mycompany.property_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<List<ErrorModel>> handleBusinessException(BusinessException bex){
        System.out.println("Business Exception is thrown.");
        return new ResponseEntity<List<ErrorModel>>(bex.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorModel>> handleFieldValidation(MethodArgumentNotValidException fieldEx){
        System.out.println("Field validation exception is thrown.");
        List<ErrorModel> errors = new ArrayList<>();
        List<FieldError> fieldErrorList = fieldEx.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrorList){
            ErrorModel error = new ErrorModel();
            error.setCode(fe.getField());
            error.setMessage(fe.getDefaultMessage());
            errors.add(error);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
