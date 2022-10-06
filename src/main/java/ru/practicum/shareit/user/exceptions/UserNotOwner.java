package ru.practicum.shareit.user.exceptions;

public class UserNotOwner extends RuntimeException {
    public UserNotOwner(String message) {
        super(message);
    }
}
