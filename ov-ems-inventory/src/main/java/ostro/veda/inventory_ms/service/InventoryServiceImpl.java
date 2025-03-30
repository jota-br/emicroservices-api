package ostro.veda.inventory_ms.service;

import org.springframework.stereotype.Service;
import ostro.veda.inventory_ms.dto.InventoryDto;
import ostro.veda.inventory_ms.model.Inventory;
import ostro.veda.inventory_ms.model.Item;
import ostro.veda.inventory_ms.model.Location;
import ostro.veda.inventory_ms.repository.InventoryRepository;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public String addInventory(InventoryDto inventoryDto) {
        Inventory inventory = build(inventoryDto);
        return inventoryRepository.save(inventory).getUuid();
    }

    private Inventory build(InventoryDto inventoryDto) {
        Inventory inventory = Inventory
                .builder()
                .uuid(UUID.randomUUID().toString())
                .build();

        List<Location> locations = inventoryDto.getLocation()
                .stream()
                .map(locationDto -> Location
                        .builder()
                        .name(locationDto.getName())
                        .uuid(UUID.randomUUID().toString())
                        .inventory(inventory)
                        .items(locationDto.getItems()
                                .stream()
                                .map(item -> Item
                                        .builder()
                                        .uuid(UUID.randomUUID().toString())
                                        .productUuid(item.getProductUuid())
                                        .productName(item.getProductName())
                                        .stock(item.getStock())
                                        .reserved(item.getReserved())
                                        .build())
                                .toList())
                        .build())
                .toList();

        locations.forEach(location -> location.getItems().forEach(item -> item.setLocation(location)));
        inventory.setLocation(locations);

        return inventory;
    }
}
