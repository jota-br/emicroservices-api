package ostro.veda.inventory_ms.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ostro.veda.inventory_ms.dto.AddInventoryDto;
import ostro.veda.inventory_ms.dto.InventoryDto;
import ostro.veda.inventory_ms.model.Inventory;
import ostro.veda.inventory_ms.repository.InventoryRepository;

import java.util.UUID;

import static ostro.veda.inventory_ms.util.ToDto.toDto;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public String addInventory(final AddInventoryDto addInventoryDto) {
        boolean exists = inventoryRepository.existsByName(addInventoryDto.getName());
        if (exists)
            throw new EntityExistsException("Inventory with name %s already exists".formatted(addInventoryDto.getName()));

        Inventory inventory = build(addInventoryDto);
        return inventoryRepository.save(inventory).getUuid();
    }

    @Override
    public InventoryDto getByUuid(String uuid) {
        Inventory inventory = inventoryRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Inventory with uuid %s not found".formatted(uuid)));

        return toDto(inventory);
    }

    private Inventory build(final AddInventoryDto addInventoryDto) {
        return Inventory
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(addInventoryDto.getName())
                .build();
    }
}
