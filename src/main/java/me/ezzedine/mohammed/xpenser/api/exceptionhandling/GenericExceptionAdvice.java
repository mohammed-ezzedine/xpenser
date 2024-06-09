package me.ezzedine.mohammed.xpenser.api.exceptionhandling;

import me.ezzedine.mohammed.xpenser.core.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GenericExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected Mono<ResponseEntity<Object>> handleNotFoundException(NotFoundException exception, ServerWebExchange request) {
        return handleExceptionInternal(exception, "\"%s\"".formatted(exception.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
