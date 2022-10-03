package ru.practicum.shareit.item.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.exceptions.ItemNullParametr;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repositores.ItemStorage;
import ru.practicum.shareit.user.exceptions.UserNotFound;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.services.UserService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Создает конструктор из тех полей которые нужны
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage; // Если стоит final для неинициализированного поля то конструктор нужен обязательно
    private final UserService userService;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) throws ItemNullParametr {
        User owner = userService.checkUser(userId);
        Item item = ItemMapper.toItem(itemDto, owner, null);
        if (item.getAvailable() == null) {
            throw new ItemNullParametr(String.format("Available not exist - %s", item.getAvailable()));
        }
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new ItemNullParametr(String.format("Name not exist - %s", item.getName()));
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new ItemNullParametr(String.format("Description not exist - %s", item.getDescription()));
        }
        itemStorage.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) throws UserNotFound {
        Item item = checkItem(itemId);
        User owner = userService.checkUser(userId);
        if (item.getOwner().equals(owner)) {
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            itemStorage.save(item);
            return ItemMapper.toItemDto(item);
        } else {
            throw new UserNotFound(String.format("User by ID: %s - is not Owner of this Item", userId));
        }
    }

    @Override
    public void deleteItem(long itemId) {
        Item item = checkItem(itemId);
        itemStorage.delete(item);
    }

    @Override
    public List<ItemDto> findAll() {
        return itemStorage.findAll().stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto findItemById(long itemId,
                                        long userId,
                                        BookingService bookingService,
                                        CommentService commentService) {
        Item item = checkItem(itemId);
        User user = userService.checkUser(userId);
        if (user.equals(item.getOwner())) {
            Booking lastBooking = bookingService.getLastBookingByItem(itemId);
            Booking nextBooking = bookingService.getNextBookingByItem(itemId);
            return ItemMapper.toItemResponseDto(item, lastBooking, nextBooking, commentService);
        }
        return ItemMapper.toItemResponseDto(item, null, null, commentService);
    }

    @Override
    public List<ItemResponseDto> findAllItemsByUserId(long userId,
                                                      BookingService bookingService,
                                                      CommentService commentService) {
        User owner = userService.checkUser(userId);
        List<Item> items = itemStorage.findAllByOwnerIdOrderByIdAsc(owner.getId());
        return items.stream().map(item -> ItemMapper.toItemResponseDto(
                        item,
                        bookingService.getLastBookingByItem(item.getId()),
                        bookingService.getNextBookingByItem(item.getId()),
                        commentService))
                .sorted(Comparator.comparing(ItemResponseDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItemsByNameAndDescription(String text) {
        if (!text.isEmpty()) {
            text = text.toLowerCase();
            return itemStorage.findAllByNameAndDescriptionLowerCase(text, text).stream()
                    .map(ItemMapper::toItemDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Item checkItem(long itemId) throws ItemNotFound {
        Optional<Item> optionalItem = itemStorage.findById(itemId);
        if (optionalItem.isPresent()) {
            return optionalItem.get();
        } else {
            throw new ItemNotFound("Item by ID: %s  - not found", itemId);
        }
    }
}
