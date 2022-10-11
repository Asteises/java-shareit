package ru.practicum.shareit.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.entity.ItemRequest;

import java.util.List;

public interface RequestStorage extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestor_IdOrderByCreatedDesc(long user);
}
