package com.example.test.api.response;

import com.example.test.utils.Errors;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Objects;

import static com.example.test.utils.Errors.E001;


@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Object handleException(HandlerMethodValidationException t) {
        return ResponseApi.negativeResponse(E001.name(), E001.getDescription(),
                ExceptionUtils.getStackTrace(t));
    }
}
