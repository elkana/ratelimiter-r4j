package com.example.ratelimiterr4j.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

// dipake utk resiliencej
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler 
{
	@ExceptionHandler({ RequestNotPermitted.class })
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public void requestNotPermitted() {
	}
}