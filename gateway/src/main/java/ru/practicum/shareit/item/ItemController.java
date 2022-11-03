package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Create item with itemDto {}, userId={}", itemDto, userId);
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @PathVariable long itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Update item with itemDto {}, itemId={}, userId={}", itemDto, itemId, userId);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable long itemId) {
        log.info("Delete item with itemId={}", itemId);
        return itemClient.deleteItem(itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@PathVariable long itemId,
                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get item with itemId={}, userId={}", itemId, userId);
        return itemClient.findItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(required = false, defaultValue = "100") Integer size) {
        log.info("Get all items by userId={}, from={}, size={}", userId, from, size);
        return itemClient.findAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByNameAndDescription(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                  @RequestParam String text,
                                                                  @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                  @Positive @RequestParam(required = false, defaultValue = "100") Integer size) {
        log.info("Search items by text={}, userId={}, from={}, size={}", text, userId, from, size);
        return itemClient.searchItemsByNameAndDescription(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@PathVariable long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody @Validated CommentDto commentDto) {
        log.info("Post comment {}, userId={}, itemId={}", commentDto, userId, itemId);
        return itemClient.postComment(itemId, userId, commentDto);
    }
}
