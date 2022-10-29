package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exception.BookingWrongTime;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.user.exceptions.UserNotBooker;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.exceptions.UserNotOwner;

import java.util.List;

@Service
public interface BookingService {

    BookingDto createBooking(BookingDto bookingDto, long userId)
            throws UserNotFound, ItemNotFound, BookingWrongTime;

    BookingResponseDto ownerDecision(long bookingId, long userId, boolean approved)
            throws NotFoundException, UserNotOwner;

    BookingResponseDto getBooking(long bookingId, long userID)
            throws NotFoundException, UserNotFound, UserNotOwner, UserNotBooker;

    List<BookingResponseDto> getAllBookingsByBooker(String state, long userId, Integer from, Integer size)
            throws UserNotFound;

    List<BookingResponseDto> getAllBookingsByOwner(String state, long userId, Integer from, Integer size)
            throws UserNotFound;

    Booking getLastBookingByItem(long itemId);

    Booking getNextBookingByItem(long itemId);

    Booking checkBooking(long bookingId) throws NotFoundException;

}
