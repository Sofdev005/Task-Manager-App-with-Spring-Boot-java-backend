package com.SoufianeDev.TaskApp.controller;

import com.SoufianeDev.TaskApp.domain.dto.ErrorDto;
import com.SoufianeDev.TaskApp.exception.TaskNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> HandleValidationExceptions(
            MethodArgumentNotValidException e
    ) {
        String errorMassage = e.getBindingResult().getFieldErrors().stream().findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("validation failed");
        ErrorDto errorDto=  new ErrorDto(errorMassage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorDto> HandleTaskNotFoundException(TaskNotFoundException ex){
        UUID taskNotFound = ex.getId();
        String errorMassage = String.format("Task with id %s not found", taskNotFound);
        ErrorDto errorDto= new ErrorDto(errorMassage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
