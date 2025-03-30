package ostro.veda.inventory_ms.service;

import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import ostro.veda.inventory_ms.dto.AddInventoryDto;
import ostro.veda.inventory_ms.model.Inventory;
import ostro.veda.inventory_ms.repository.InventoryRepository;

import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public String addInventory(final AddInventoryDto addInventoryDto) {
        boolean exists = inventoryRepository.existsByName(addInventoryDto.getName());
        if (exists) throw new EntityExistsException("Inventory with name %s already exists".formatted(addInventoryDto.getName()));

        Inventory inventory = build(addInventoryDto);
        return inventoryRepository.save(inventory).getUuid();
    }

    private Inventory build(final AddInventoryDto addInventoryDto) {
        return Inventory
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(addInventoryDto.getName())
                .build();
    }
}
