package ru.practicum.shareit.item.comment;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exceptions.ItemNotFound;
import ru.practicum.shareit.user.exceptions.UserNotBooker;
import ru.practicum.shareit.user.exceptions.UserNotFound;

import java.util.List;

@Service
public interface CommentService {

    CommentDto postComment(long itemId, long userId, CommentDto text)
            throws UserNotFound, ItemNotFound, UserNotBooker;

    List<CommentDto> getAllCommentsByItem(long itemId);
}
