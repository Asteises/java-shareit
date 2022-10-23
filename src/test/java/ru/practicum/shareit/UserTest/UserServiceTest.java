package ru.practicum.shareit.UserTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repositories.UserStorage;
import ru.practicum.shareit.user.services.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserServiceImpl userService;

    @Mock
    private UserStorage userStorage;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userStorage);
    }

    @Test
    void findUserByIdTest() {
        // Assign
        var user = new User();
        user.setId(1L);
        user.setName("TestUserName");
        user.setEmail("test@email.ru");

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        var result = userService.findUserById(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }
}
