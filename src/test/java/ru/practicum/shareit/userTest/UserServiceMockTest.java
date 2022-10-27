package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repositories.UserStorage;
import ru.practicum.shareit.user.services.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceMockTest {

    private UserServiceImpl userService;

    @Mock
    private UserStorage userStorage;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userStorage);
    }

    @Test
    public void createUserTest() {
        // Assign
        var user = getTestUser();
        var userDto = getTestUserDto();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        when(userStorage.save(any(User.class))).thenReturn(user);

        // Act
        var result = userService.createUser(userDto);

        // Assert
        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    public void updateUserTest() {
        // Assign
        var user = getTestUser();
        var userDto = getTestUserDto();

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        var result = userService.updateUser(userDto, user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    public void deleteUserTest() {
        // Assign
        var user = getTestUser();

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(user.getId());

        // Assert
        Mockito.verify(userStorage, Mockito.times(1)).delete(user);
    }

    @Test
    public void findAllUsersTest() {
        // Assign
        User user1 = getTestUser();
        User user2 = getTestUser();
        user2.setId(2L);
        List<User> users = List.of(user1, user2);

        when(userStorage.findAll()).thenReturn(users);

        // Act
        List<UserDto> actualUsers = userService.findAllUsers();

        // Assert
        assertNotNull(actualUsers);
        assertEquals(users.size(), actualUsers.size());
    }

    @Test
    public void findUserByIdTest() {
        // Assign
        var user = getTestUser();

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        var result = userService.findUserById(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("testUser@email.ru");
        user.setName("Test User Name");
        return user;
    }

    private UserDto getTestUserDto() {
        return new UserDto(1L, "Test UserDto Name", "testUserDto@email.ru");
    }
}
