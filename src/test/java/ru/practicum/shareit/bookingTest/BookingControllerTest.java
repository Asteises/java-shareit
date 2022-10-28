package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

    private final BookingDto bookingDto1 = new BookingDto(1L,
            1L,
            1L,
            LocalDateTime.now().plusSeconds(10),
            LocalDateTime.now().plusSeconds(20),
            BookingStatus.WAITING);

    @Test
    public void createBookingTest() throws Exception {
        // Assign
        Booking booking = getTestBooking();

        when(bookingService.createBooking(any(BookingDto.class), anyLong()))
                .thenReturn(BookingMapper.toBookingDto(booking));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booking.getBooker().getId())
                        .content(mapper.writeValueAsString(BookingMapper.toBookingDto(booking)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.end", is(booking.getEnd().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.itemId", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    public void ownerDecisionTest() throws Exception {
        // Assign
        Booking booking = getTestBooking();
        booking.setStatus(BookingStatus.APPROVED);
        Long ownerId = 1L;

        when(bookingService.ownerDecision(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(BookingMapper.toBookingResponseDto(booking));
        // Act
        mvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", ownerId)
                        .content(mapper.writeValueAsString(BookingMapper.toBookingResponseDto(booking)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString()), BookingStatus.class));
    }

    private Booking getTestBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusSeconds(10));
        booking.setEnd(LocalDateTime.now().plusSeconds(20));
        booking.setItem(getTestItem());
        booking.setBooker(getTestUser());
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User Name 1");
        user.setEmail("testUser1@email.ru");
        return user;
    }

    private Item getTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item Name 1");
        item.setDescription("Test Item Description 1");
        item.setOwner(getTestUser());
        item.setRequest(null);
        return item;
    }

}
