package ru.practicum.shareit.item.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.exceptions.ItemNullParametr;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotBooker;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentStorage commentStorage;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Override
    public CommentDto postComment(long itemId, long userId, CommentDto commentDto)
            throws UserNotBooker, ItemNullParametr {
        if (commentDto.getText().isEmpty()) {
            throw new BadRequestException("Comment is empty text");
        }
        User user = userService.checkUser(userId);
        Item item = itemService.checkItem(itemId);
        List<Booking> bookings = bookingService.findAllBookingByItemIdAndBooker(itemId, userId);
        if (!bookings.isEmpty()) {
            if (bookings.stream().anyMatch(booking ->
                    (!BookingStatus.REJECTED.equals(booking.getStatus())
                            && !BookingStatus.WAITING.equals(booking.getStatus())) &&
                            !booking.getStart().isAfter(LocalDateTime.now()))) {
                Comment comment = new Comment();
                comment.setAuthor(user);
                comment.setItem(item);
                comment.setText(commentDto.getText());
                comment = commentStorage.save(comment);
                return CommentMapper.toCommentDto(comment);
            }
            throw new BadRequestException("ыыыы");
        } else {
            throw new UserNotBooker("This User not Booker for this Item", userId);
        }
    }

    @Override
    public List<CommentDto> getAllCommentsByItem(long itemId) {
        Item item = itemService.checkItem(itemId);
        List<Comment> comments = commentStorage.getCommentsByItem_idOrderByCreatedDesc(itemId);
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
