package ru.practicum.shareit.user.exceptions;

public class UserDuplicatedEmail extends IllegalArgumentException {

    public UserDuplicatedEmail(String format, String s) {
        super(s);
    }
}
