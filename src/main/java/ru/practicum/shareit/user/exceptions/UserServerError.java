package ru.practicum.shareit.user.exceptions;

public class UserServerError extends RuntimeException {

    public UserServerError(String message) {
        super(message);
    }
}
