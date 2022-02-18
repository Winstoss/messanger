package com.windstoss.messanger.api.exception.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(Throwable cause){
        super(cause);
    }

    public InvalidCredentialsException() {
        super();
    }

}
