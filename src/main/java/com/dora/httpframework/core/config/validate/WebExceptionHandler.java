package com.dora.httpframework.core.config.validate;

import com.dora.httpframework.core.base.RepResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @Describe TODO:
 * @Author dora 1.0.1
 **/
@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public RepResult BindExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return RepResult.validateFail(message);
    }

    //@RequestParam Error
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public RepResult ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return RepResult.validateFail(message);
    }

    // Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public RepResult MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return RepResult.validateFail(message);
    }
}
