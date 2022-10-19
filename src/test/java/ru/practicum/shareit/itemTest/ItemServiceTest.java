package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repositories.BookingStorage;
import ru.practicum.shareit.item.comment.CommentStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repositories.ItemStorage;
import ru.practicum.shareit.item.services.ItemServiceImpl;
import ru.practicum.shareit.request.model.entity.ItemRequest;
import ru.practicum.shareit.request.repositories.RequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    // Мокаем все сервисы и репозитории, которые не надо тестировать
    @Mock
    private ItemStorage itemStorage;

    @Mock
    private UserService userService;

    @Mock
    private BookingStorage bookingStorage;

    @Mock
    private CommentStorage commentStorage;

    @Mock
    private RequestStorage requestStorage;

    private ItemServiceImpl itemService;

    @BeforeEach
    void init() {
        // Инициализируем сервис бинами заглушками
        itemService = new ItemServiceImpl(
                itemStorage,
                userService,
                bookingStorage,
                commentStorage,
                requestStorage);
    }

    @Test
    public void addItemTest() {

        Mockito.when(userService.checkUser(anyLong())) // Когда вызовется метод checkUser() с любым long...
                .thenReturn(getTestUser()); // ... вернуть тестовое значение
        Mockito.when(requestStorage.findById(anyLong())) // Когда вызовется метод findById() с любым long...
                .thenReturn(getTestItemRequest()); // ... вернуть тестовое значение

        ItemDto actualDto = itemService.createItem(getTestItemDto(), 1L); // Вызываем тестируемый метод с тестовыми данными

        Assertions.assertEquals(actualDto.getName(), "TestName"); // Сравниваем актуальные значения с предполагаемыми
        Assertions.assertEquals(actualDto.getDescription(), "Description");
        // Строки в сравнениях либо вынести в константы, либо брать из тестовой DTO. Написал их здесь для наглядности
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("@");
        user.setName("Test");
        return user;
    }

    private ItemDto getTestItemDto() {
        return new ItemDto(1L, "TestName", "Description", Boolean.TRUE, 1L);
    }

    private Optional<ItemRequest> getTestItemRequest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(getTestUser());
        itemRequest.setId(1L);
        itemRequest.setDescription("Description");
        itemRequest.setCreated(LocalDateTime.now());
        return Optional.of(itemRequest);
    }
}
