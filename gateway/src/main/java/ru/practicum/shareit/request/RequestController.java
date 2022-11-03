package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Post request {}, userId={}", itemRequestDto, userId);
        return requestClient.createRequest(itemRequestDto, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllResponsesForAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                @Positive @RequestParam(required = false, defaultValue = "100") Integer size) {
        log.info("Get all responses by userId={}, from={}, size={}", userId, from, size);
        return requestClient.getAllResponsesForAllRequests(userId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(required = false, defaultValue = "100") Integer size) {
        log.info("Get all requests by userId={}, from={}, size={}", userId, from, size);
        return requestClient.getAllRequestsOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable long requestId) {
        log.info("Get request requestId={}, userId={}", userId, requestId);
        return requestClient.getRequestById(userId, requestId);
    }
}
