package com.server.server.controller;


import com.server.server.exceptions.NoteImageNotFoundException;
import com.server.server.exceptions.NoteNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NoteNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Note not found",
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({NoteImageNotFoundException.class})
    protected ResponseEntity<Object> handleImageFailed(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Image failed",
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
