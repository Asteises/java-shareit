package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "100") Integer size) {
        BookingStatus state = checkState(stateParam);
        checkPaging(from, size);
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                        @RequestHeader("X-Sharer-User-Id") long userId,
                                                        @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(required = false, defaultValue = "100") Integer size) {
        BookingStatus state = checkState(stateParam);
        checkPaging(from, size);
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookingsByOwner(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid BookItemRequestDto requestDto) {
        validateDates(requestDto.getStart(), requestDto.getEnd());
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.createBooking(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> ownerDecision(@PathVariable long bookingId,
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam boolean approved) {
        log.info("Patch booking by owner decision bookingId={}, userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.ownerDecision(bookingId, userId, approved);
    }

    private BookingStatus checkState(String state) {
        return BookingStatus.from(state)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + state));
    }

    public static void checkPaging(Integer from, Integer size) throws BadRequestException {
        if (from == null && size == null) {
            return;
        }
        if (size == 0) {
            throw new BadRequestException("size == 0");
        }
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        LocalDateTime current = LocalDateTime.now();
        if ((start.isEqual(current) || start.isAfter(current)) && end.isAfter(start)) {
            System.out.println("Time is OK");;
        } else {
            throw new BadRequestException("Booking wrong Time");
        }
    }
}
