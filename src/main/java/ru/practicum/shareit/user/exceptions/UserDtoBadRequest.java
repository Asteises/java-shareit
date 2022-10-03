package ru.practicum.shareit.user.exceptions;

public class UserDtoBadRequest extends RuntimeException {

    public UserDtoBadRequest(String message) {
        super(message);
    }
}
