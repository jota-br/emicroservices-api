package ostro.veda.inventory_ms.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ostro.veda.inventory_ms.dto.AddItemDto;
import ostro.veda.inventory_ms.dto.ItemDto;
import ostro.veda.inventory_ms.dto.UpdateProductNameDto;
import ostro.veda.inventory_ms.dto.UpdateProductStockDto;
import ostro.veda.inventory_ms.model.Item;
import ostro.veda.inventory_ms.model.Location;
import ostro.veda.inventory_ms.repository.ItemRepository;
import ostro.veda.inventory_ms.repository.LocationRepository;

import java.util.UUID;

import static ostro.veda.inventory_ms.util.ToDto.toDto;

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
    public ItemDto getByUuid(String uuid) {
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
    public void updateReserve(UpdateProductStockDto updateProductStockDto) {
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
