package ru.practicum.shareit.booking.exception;

public class BookingNotFound extends RuntimeException {

    public BookingNotFound(String message) {
        super(message);
    }
}
