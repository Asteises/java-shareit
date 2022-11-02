package ru.practicum.shareit.booking;

//400
public class BadRequestException extends RuntimeException {

    public BadRequestException(String error) {
        super(error);
    }
}
