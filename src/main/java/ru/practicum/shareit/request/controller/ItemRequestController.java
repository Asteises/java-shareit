package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.RequestWithResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestBody ItemRequestDto itemRequestDto,
                                        @RequestHeader ("X-Sharer-User-Id") long userId) {
        return requestService.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<RequestWithResponseDto> getAllResponsesForAllRequests(@RequestHeader ("X-Sharer-User-Id") long userId){
        return requestService.getAllResponsesForAllRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader ("X-Sharer-User-Id") long userId,
                                               @RequestParam(required = false) @Min(0) Integer from,
                                               @RequestParam(required = false) @Min(0) Integer size) {
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestWithResponseDto getRequestById(@PathVariable long requestId) {
        return requestService.getRequestById(requestId);
    }
}
