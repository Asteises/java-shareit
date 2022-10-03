package ru.practicum.shareit.item.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) throws UserNotFound {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId) throws ItemNotFound, UserNotFound {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable long itemId) {
        itemService.deleteItem(itemId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponseDto findItemById(@PathVariable long itemId,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findItemById(itemId, userId, bookingService, commentService);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemResponseDto> findAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAllItemsByUserId(userId, bookingService, commentService);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItemsByNameAndDescription(@RequestParam String text) {
        return itemService.searchItemsByNameAndDescription(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto postComment(@PathVariable long itemId,
                                  @RequestHeader("X-Sharer-User-Id") long userId,
                                  @RequestBody @Valid CommentDto commentDto) {
        return commentService.postComment(itemId, userId, commentDto);
    }
}
