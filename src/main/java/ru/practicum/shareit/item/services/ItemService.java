package ru.practicum.shareit.item.services;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.List;

@Service
public interface ItemService {

    ItemDto createItem(ItemDto itemDto, long userId) throws UserNotFound;

    ItemDto updateItem(ItemDto itemDto, long itemId, long userId) throws ItemNotFound, UserNotFound;

    void deleteItem(long itemId) throws ItemNotFound;

    List<ItemDto> findAll();

    ItemResponseDto findItemById(long itemId,
                                 long userId,
                                 BookingService bookingService,
                                 CommentService commentService) throws ItemNotFound;

    List<ItemResponseDto> findAllItemsByUserId(long userId,
                                               BookingService bookingService,
                                               CommentService commentService);

    List<ItemDto> searchItemsByNameAndDescription(String text);

    Item checkItem(long itemId);
}
