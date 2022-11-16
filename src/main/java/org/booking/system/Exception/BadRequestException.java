package org.booking.system.Exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(final String message)
    {
        super(message);
    }
}
