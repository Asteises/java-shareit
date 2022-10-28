package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repositories.BookingStorage;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.request.model.entity.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookingServiceMockTest {

    @Mock
    private BookingStorage bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;

    private BookingServiceImpl bookingService;

    @BeforeEach
    void init() {
        // Инициализируем сервис бинами заглушками
        bookingService = new BookingServiceImpl(
                bookingRepository,
                userService,
                itemService) {
        };
    }

    @Test
    public void createBookingTest() throws Exception {
        // Assign
        User owner = getTestUser();
        User booker = getTestUser();
        booker.setId(2L);
        ItemRequest itemRequest = getTestItemRequest(booker);
        Item item = getTestItem(owner, itemRequest);
        Booking booking = getTestBooking(booker, item);

        Mockito.when(userService.checkUser(anyLong())) // Когда вызовется метод checkUser() с любым long...
                .thenReturn(booker); // ... вернуть тестовое значение
        Mockito.when(itemService.checkItem(anyLong()))
                .thenReturn(item);
        Mockito.when(bookingRepository.findByItemAndBooker(any(Item.class), any(User.class)))
                .thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        // Act
        BookingDto bookingDto = bookingService.createBooking(BookingMapper.toBookingDto(booking), booker.getId());

        // Assert
        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals(bookingDto.getId(), booking.getId());
        Assertions.assertEquals(bookingDto.getBookerId(), booking.getBooker().getId());
        Assertions.assertEquals(bookingDto.getItemId(), item.getId());
        Assertions.assertEquals(bookingDto.getStatus(), booking.getStatus());
    }

    @Test
    public void ownerDecisionTest() throws Exception {
        // Assign
        User owner = getTestUser();
        User booker = getTestUser();
        booker.setId(2L);
        ItemRequest itemRequest = getTestItemRequest(booker);
        Item item = getTestItem(owner, itemRequest);
        Booking booking = getTestBooking(booker, item);

        Mockito.when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        // Act

        BookingResponseDto bookingResponseDto = bookingService.ownerDecision(booking.getId(), owner.getId(), true);

        // Assert
        Assertions.assertNotNull(bookingResponseDto);
        Assertions.assertEquals(bookingResponseDto.getId(), booking.getId());
        Assertions.assertEquals(bookingResponseDto.getBooker().getId(), booking.getBooker().getId());
        Assertions.assertEquals(bookingResponseDto.getItem().getId(), item.getId());
        Assertions.assertEquals(bookingResponseDto.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    public void getBookingTest() throws Exception {
        // Assign
        User owner = getTestUser();
        User booker = getTestUser();
        booker.setId(2L);
        ItemRequest itemRequest = getTestItemRequest(booker);
        Item item = getTestItem(owner, itemRequest);
        Booking booking = getTestBooking(booker, item);

        Mockito.when(userService.checkUser(anyLong())) // Когда вызовется метод checkUser() с любым long...
                .thenReturn(booker); // ... вернуть тестовое значение
        Mockito.when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        // Act

        BookingResponseDto bookingResponseDto = bookingService.getBooking(booking.getId(), booker.getId());

        // Assert
        Assertions.assertNotNull(bookingResponseDto);
        Assertions.assertEquals(bookingResponseDto.getId(), booking.getId());
        Assertions.assertEquals(bookingResponseDto.getBooker().getId(), booking.getBooker().getId());
        Assertions.assertEquals(bookingResponseDto.getItem().getId(), item.getId());
        Assertions.assertEquals(bookingResponseDto.getStatus(), BookingStatus.WAITING);
    }

    @Test
    public void getAllBookingsByBookerTest() throws Exception {
        // Assign
        User owner = getTestUser();
        User booker = getTestUser();
        booker.setId(2L);
        ItemRequest itemRequest = getTestItemRequest(booker);
        Item item = getTestItem(owner, itemRequest);
        Booking booking1 = getTestBooking(booker, item);
        Booking booking2 = getTestBooking(booker, item);
        booking2.setId(2L);

        BookingResponseDto bookingResponseDto1 = BookingMapper.toBookingResponseDto(booking1);
        BookingResponseDto bookingResponseDto2 = BookingMapper.toBookingResponseDto(booking2);
        List<BookingResponseDto> bookings = List.of(bookingResponseDto1, bookingResponseDto2);

        Mockito.when(userService.checkUser(anyLong()))
                .thenReturn(booker);
        Mockito.when(bookingRepository.findAllByBookerOrderByStartDesc(any(User.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1, booking2)));

        // Act
        List<BookingResponseDto> actualBookings = bookingService.getAllBookingsByBooker("ALL", booker.getId(), 0, 10);

        // Assert
        Assertions.assertNotNull(actualBookings);
        Assertions.assertEquals(bookings.size(), actualBookings.size());
    }

    @Test
    public void getAllBookingsByOwnerTest() throws Exception {
        // Assign
        User owner = getTestUser();
        User booker = getTestUser();
        booker.setId(2L);
        ItemRequest itemRequest = getTestItemRequest(booker);
        Item item = getTestItem(owner, itemRequest);
        Booking booking1 = getTestBooking(booker, item);
        Booking booking2 = getTestBooking(booker, item);
        booking2.setId(2L);

        BookingResponseDto bookingResponseDto1 = BookingMapper.toBookingResponseDto(booking1);
        BookingResponseDto bookingResponseDto2 = BookingMapper.toBookingResponseDto(booking2);
        List<BookingResponseDto> bookings = List.of(bookingResponseDto1, bookingResponseDto2);

        Mockito.when(userService.checkUser(anyLong()))
                .thenReturn(owner);
        Mockito.when(bookingRepository.findAllByBookerOrderByStartDesc(any(User.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1, booking2)));

        // Act
        List<BookingResponseDto> actualBookings = bookingService.getAllBookingsByBooker("ALL", booker.getId(), 0, 10);

        // Assert
        Assertions.assertNotNull(actualBookings);
        Assertions.assertEquals(bookings.size(), actualBookings.size());
    }

    @Test
    public void getLastBookingByItemTest() throws Exception {
        // Assign
        User owner = getTestUser();
        User booker = getTestUser();
        booker.setId(2L);
        ItemRequest itemRequest = getTestItemRequest(booker);
        Item item = getTestItem(owner, itemRequest);
        Booking booking1 = getTestBooking(booker, item);

        Mockito.when(bookingRepository.findFirstByItem_idAndEndBeforeOrderByEndDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(booking1);

        // Act
        Booking actualBooking = bookingService.getLastBookingByItem(item.getId());

        // Assert
        Assertions.assertNotNull(actualBooking);
        Assertions.assertEquals(booking1.getId(), actualBooking.getId());
        Assertions.assertEquals(booking1.getBooker().getId(), actualBooking.getBooker().getId());
        Assertions.assertEquals(booking1.getItem().getId(), actualBooking.getItem().getId());
        Assertions.assertEquals(booking1.getStatus(), actualBooking.getStatus());
    }

    @Test
    public void getNextBookingByItem() throws Exception {
        // Assign
        User owner = getTestUser();
        User booker = getTestUser();
        booker.setId(2L);
        ItemRequest itemRequest = getTestItemRequest(booker);
        Item item = getTestItem(owner, itemRequest);
        Booking booking1 = getTestBooking(booker, item);

        Mockito.when(bookingRepository.findFirstByItem_idAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(booking1);

        // Act
        Booking actualBooking = bookingService.getNextBookingByItem(item.getId());

        // Assert
        Assertions.assertNotNull(actualBooking);
        Assertions.assertEquals(booking1.getId(), actualBooking.getId());
        Assertions.assertEquals(booking1.getBooker().getId(), actualBooking.getBooker().getId());
        Assertions.assertEquals(booking1.getItem().getId(), actualBooking.getItem().getId());
        Assertions.assertEquals(booking1.getStatus(), actualBooking.getStatus());
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("@");
        user.setName("Test");
        return user;
    }

    private ItemRequest getTestItemRequest(User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Test Description ItemRequest");
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    private Item getTestItem(User owner, ItemRequest itemRequest) {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Name Item");
        item.setDescription("Test Description Item");
        item.setOwner(owner);
        item.setRequest(itemRequest);
        item.setAvailable(true);
        return item;
    }

    private Booking getTestBooking(User booker, Item item) {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart((LocalDateTime.now().plusSeconds(10)));
        booking.setEnd((LocalDateTime.now().plusSeconds(20)));
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }
}
