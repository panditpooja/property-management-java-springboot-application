package com.mycompany.property_management.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<List<ErrorModel>> handleBusinessException(BusinessException bex){
        for (ErrorModel em : bex.getErrors()){
            logger.debug("Business Exception is thrown level-debug {} - {} ",em.getCode(), em.getMessage());
            logger.info("Business Exception is thrown level-info {} - {} ",em.getCode(), em.getMessage());
            logger.warn("Business Exception is thrown level-warn {} - {} ",em.getCode(), em.getMessage());
            logger.error("Business Exception is thrown - level-error {} - {} ",em.getCode(), em.getMessage());
        }
        logger.debug("Business Exception is thrown. {} ",bex.getErrors());
        logger.info("Business Exception is thrown. {} ",bex.getErrors());
        return new ResponseEntity<List<ErrorModel>>(bex.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorModel>> handleFieldValidation(MethodArgumentNotValidException fieldEx){
        System.out.println("Field validation exception is thrown.");
        List<ErrorModel> errors = new ArrayList<>();
        List<FieldError> fieldErrorList = fieldEx.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrorList){
            logger.debug("Inside field validation: {} - {}",fe.getField(), fe.getDefaultMessage());
            logger.info("Inside field validation: {} - {}",fe.getField(), fe.getDefaultMessage());
            ErrorModel error = new ErrorModel();
            error.setCode(fe.getField());
            error.setMessage(fe.getDefaultMessage());
            errors.add(error);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
