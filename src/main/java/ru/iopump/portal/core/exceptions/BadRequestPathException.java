package ru.iopump.portal.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestPathException extends RuntimeException {
    public BadRequestPathException(String message) {
        super(message);
    }
}
