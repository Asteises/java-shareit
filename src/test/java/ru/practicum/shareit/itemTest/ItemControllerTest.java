package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controllers.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper; // Встроенный в Spring маппер для преобразования объекта в строку формата JSON

    @MockBean
    private ItemService itemService; // Мокаем бин

    @Autowired
    private MockMvc mvc;

    @Test
    public void createItemTest() throws Exception {

        // Создаём тестовое DTO
        ItemDto itemDto = new ItemDto(1L, "TestName", "Description", Boolean.TRUE, 1L);

        // Так как мы тестируем только контроллеры, то сервис надо замокать
        Mockito.when(itemService.createItem(Mockito.any(ItemDto.class), anyLong())) // когда вызывается метод createItem() с любым классом ItemDto и с любым long...
                .thenReturn(itemDto); // ... то возвращаем данное DTO

        mvc.perform(post("/items") // Отправляем запрос на этот URL
                    .header("X-Sharer-User-Id", 1L) // Указываем header
                    .content(mapper.writeValueAsString(itemDto)) // Передаём itemDto в body, при помощи маппера преобразуем dto в строку формата JSON
                    .characterEncoding(StandardCharsets.UTF_8) // Кодировка
                    .contentType(MediaType.APPLICATION_JSON) // Возвращаем json
                    .accept(MediaType.APPLICATION_JSON)) // принимаем json
                .andExpect(status().isCreated()) // Должен прийти ответ 201
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class)) // Проверяем поле id  в JSON
                .andExpect(jsonPath("$.name", is(itemDto.getName()))) // И тд проверяем все поля
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    public void updateItemTest() throws Exception {

        ItemDto itemDto = new ItemDto(1L, "TestName", "Description", Boolean.TRUE, 1L);

        Mockito.when(itemService.createItem(Mockito.any(ItemDto.class), anyLong())).thenReturn(itemDto);

//        mvc.perform(patch("/items/{itemId}", i).()
//
//        )
    }
}
