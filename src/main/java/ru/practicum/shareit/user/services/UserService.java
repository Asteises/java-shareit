package ru.practicum.shareit.user.services;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserDtoBadRequest;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public interface UserService {

    UserDto createUser(UserDto userDto) throws UserDtoBadRequest;

    UserDto updateUser(UserDto userDto, long userId);

    void deleteUser(long userId);

    List<UserDto> findAllUsers();

    UserDto findUserById(long userId);

    User checkUser(long userId) throws UserNotFound;


}
