package se.datasektionen.calypso.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class FailedValidationException extends RuntimeException {
    public FailedValidationException(Exception cause) {
        super("Validation failed", cause);
    }
}
