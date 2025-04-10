package io.github.jotabrc.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.jotabrc.dto.AddItemDto;
import io.github.jotabrc.dto.ItemDto;
import io.github.jotabrc.dto.UpdateProductNameDto;
import io.github.jotabrc.dto.UpdateProductStockDto;
import io.github.jotabrc.model.Item;
import io.github.jotabrc.model.Location;
import io.github.jotabrc.repository.ItemRepository;
import io.github.jotabrc.repository.LocationRepository;

import java.util.UUID;

import static io.github.jotabrc.util.ToDto.toDto;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, LocationRepository locationRepository) {
        this.itemRepository = itemRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public String add(final AddItemDto addItemDto) {
        Location location = locationRepository.findByUuid(addItemDto.getLocationUuid())
                .orElseThrow(() -> new EntityNotFoundException("Location with uuid %s not found"
                        .formatted(addItemDto.getLocationUuid())));

        boolean exists = itemRepository.existsByUuid(addItemDto.getProductUuid());
        if (exists) throw new EntityExistsException("Item with uuid %s already exists".formatted(addItemDto.getProductUuid()));

        Item item = build(addItemDto, location);
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
                .findByLocationUuidAndProductUuid(
                        updateProductStockDto.getLocationUuid(), updateProductStockDto.getProductUuid()
                )
                .orElseThrow(() -> new EntityNotFoundException("Product with uuid %s in Location with uuid %s not found"
                        .formatted(updateProductStockDto.getProductUuid(), updateProductStockDto.getLocationUuid())));

        item.setStock(item.getStock() + updateProductStockDto.getQuantity());
        itemRepository.save(item);
    }

    @Override
    public void updateName(final UpdateProductNameDto updateProductNameDto) {
        Item item = itemRepository
                .findByLocationUuidAndProductUuid(
                        updateProductNameDto.getLocationUuid(), updateProductNameDto.getProductUuid()
                )
                .orElseThrow(() -> new EntityNotFoundException("Product with uuid %s in Location with uuid %s not found"
                        .formatted(updateProductNameDto.getProductUuid(), updateProductNameDto.getLocationUuid())));

        item.setProductName(updateProductNameDto.getProductName());
        itemRepository.save(item);
    }

    @Override
    public void updateReserve(final UpdateProductStockDto updateProductStockDto) {
        Item item = itemRepository
                .findByLocationUuidAndProductUuid(
                        updateProductStockDto.getLocationUuid(), updateProductStockDto.getProductUuid()
                )
                .orElseThrow(() -> new EntityNotFoundException("Product with uuid %s in Location with uuid %s not found"
                        .formatted(updateProductStockDto.getProductUuid(), updateProductStockDto.getLocationUuid())));

        item.setStock(item.getReserved() + updateProductStockDto.getQuantity());
        itemRepository.save(item);
    }

    private Item build(final AddItemDto addItemDto, final  Location location) {
        return Item
                .builder()
                .uuid(UUID.randomUUID().toString())
                .productUuid(addItemDto.getProductUuid())
                .productName(addItemDto.getProductName())
                .stock(addItemDto.getStock())
                .reserved(addItemDto.getReserved())
                .location(location)
                .build();
    }
}
