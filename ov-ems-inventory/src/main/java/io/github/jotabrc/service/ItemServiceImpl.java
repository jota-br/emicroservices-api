package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddItemDto;
import io.github.jotabrc.dto.ItemDto;
import io.github.jotabrc.dto.UpdateProductNameDto;
import io.github.jotabrc.dto.UpdateProductStockDto;
import io.github.jotabrc.model.Item;
import io.github.jotabrc.repository.ItemRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.github.jotabrc.util.ToDto.toDto;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public String add(final AddItemDto addItemDto) {

        boolean exists = itemRepository.existsByUuid(addItemDto.getUuid());
        if (exists) throw new EntityExistsException("Item with uuid %s already exists".formatted(addItemDto.getUuid()));

        Item item = build(addItemDto);
        return itemRepository.save(item).getUuid();
    }

    @Override
    public ItemDto getByUuid(final String uuid) {
        Item item = itemRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Item with uuid %s not found"
                        .formatted(uuid)));

        return toDto(item);
    }

    @Override
    public void updateStock(final UpdateProductStockDto updateProductStockDto) {
        Item item = itemRepository
                .findByUuid(updateProductStockDto.getProductUuid())
                .orElseThrow(() -> new EntityNotFoundException("Product with uuid %s not found"
                        .formatted(updateProductStockDto.getProductUuid())));

        item.setStock(item.getStock() + updateProductStockDto.getQuantity());
        itemRepository.save(item);
    }

    @Override
    public void updateName(final UpdateProductNameDto updateProductNameDto) {
        Item item = itemRepository
                .findByUuid(updateProductNameDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Product with uuid %s not found"
                        .formatted(updateProductNameDto.getUuid())));

        item.setName(updateProductNameDto.getName());
        itemRepository.save(item);
    }

    @Override
    public void updateReserve(final UpdateProductStockDto updateProductStockDto) {
        Item item = itemRepository
                .findByUuid(updateProductStockDto.getProductUuid())
                .orElseThrow(() -> new EntityNotFoundException("Product with uuid %s not found"
                        .formatted(updateProductStockDto.getProductUuid())));

        item.setStock(item.getReserved() + updateProductStockDto.getQuantity());
        itemRepository.save(item);
    }

    private Item build(final AddItemDto addItemDto) {
        return Item
                .builder()
                .uuid(addItemDto.getUuid())
                .name(addItemDto.getName())
                .build();
    }
}
