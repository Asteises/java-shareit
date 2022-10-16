package ru.practicum.shareit.request.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.entity.ItemRequest;

import java.util.List;

public interface RequestStorage extends JpaRepository<ItemRequest, Long> {

    Page<ItemRequest> findAllByRequestor_IdOrderByCreatedDesc(long user, Pageable pageable);
}
