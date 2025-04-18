package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddItemDto;
import io.github.jotabrc.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void add() {
        when(itemRepository.existsByUuid("uuid")).thenReturn(false);

        AddItemDto addItemDto = AddItemDto
                .builder()
                .uuid("uuid")
                .name("Item Name")
                .build();

        String uuid = itemService.add(addItemDto);
        assert uuid.equals(addItemDto.getUuid());
    }

    @Test
    void getByUuid() {
    }

    @Test
    void updateStock() {
    }

    @Test
    void updateName() {
    }

    @Test
    void updateReserve() {
    }

    @Test
    void testUpdateReserve() {
    }
}