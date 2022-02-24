package com.windstoss.messanger.api.exception;

import com.windstoss.messanger.api.exception.exceptions.*;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConflict(RuntimeException ex) {
        return new ResponseEntity<>("Message type is not specified", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(UserCreationException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "User with such username is already exists",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(UserNotFoundException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "User with such username doesn't exist",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(ChatNotFoundException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "Chat not found",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(GroupChatPrivilegesException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "You should be admin to do so",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(UserIsAlreadyAdminException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "User is already admin in this chat",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(ContentIsEmptyException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "Content is empty",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(MessageAuthorityException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "Requester is not author of this message",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(MessageNotFoundException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "Message not found",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(ChatAlreadyExistsException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "Chat already exists",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(UserAbsenceException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "User is not present in this chat",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(UserIsAlreadyPresentException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "User already present in this chat",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(InvalidCredentialsException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "Such credentials are incorrect",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(InternalStorageException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "File storage in unreachable at this time",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(MessageIsEqualException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "Message is equal to previous",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRequestException(MessageIsEmptyException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        RequestException requestException = new RequestException( "Message is empty",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(requestException, httpStatus);
    }

}
