package ru.practicum.shareit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingWrongTime;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.exceptions.ItemNullParametr;
import ru.practicum.shareit.user.exceptions.UserDtoBadRequest;
import ru.practicum.shareit.user.exceptions.UserDuplicatedEmail;
import ru.practicum.shareit.user.exceptions.UserNotBooker;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.exceptions.UserNotOwner;
import ru.practicum.shareit.user.exceptions.UserServerError;

@RestControllerAdvice
public class ErrorHandler {

    //404
    @ExceptionHandler({NotFoundException.class, UserNotFound.class, ItemNotFound.class, NotFoundException.class})
    public ResponseEntity<String> userNotFoundHandler(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    //500
    @ExceptionHandler({UserDuplicatedEmail.class})
    public ResponseEntity<String> userDuplicatedEmail(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    //400
    @ExceptionHandler({ItemNullParametr.class})
    public ResponseEntity<ErrorResponse> itemBadParam(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    //400
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> badRequestHandler(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({UserDtoBadRequest.class})
    public ResponseEntity<String> userBadRequest(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({UserServerError.class})
    public ResponseEntity<String> userServerError(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler({UserNotOwner.class})
    public ResponseEntity<String> wrongOwner(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({UserNotBooker.class})
    public ResponseEntity<String> wrongBooker(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({BookingWrongTime.class})
    public ResponseEntity<String> wrongTime(final RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
