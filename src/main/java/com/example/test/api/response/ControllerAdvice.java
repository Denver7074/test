package com.example.test.api.response;

import com.example.test.utils.Error;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.test.utils.Error.E001;


@RestControllerAdvice
public class ControllerAdvice {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object handleException(MissingServletRequestParameterException ex) {
        return ResponseApi.negativeResponse(E001.name(),
                String.format(E001.getDescription(), ex.getParameterName()),
                ExceptionUtils.getStackTrace(ex));
    }
}
