package ostro.veda.inventory_ms.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ostro.veda.inventory_ms.dto.AddLocationDto;
import ostro.veda.inventory_ms.dto.UpdateLocationDto;
import ostro.veda.inventory_ms.model.Inventory;
import ostro.veda.inventory_ms.model.Location;
import ostro.veda.inventory_ms.repository.InventoryRepository;
import ostro.veda.inventory_ms.repository.LocationRepository;

import java.util.UUID;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final InventoryRepository inventoryRepository;

    public LocationServiceImpl(LocationRepository locationRepository, InventoryRepository inventoryRepository) {
        this.locationRepository = locationRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public String add(final AddLocationDto addLocationDto) {
        boolean exists = locationRepository.existsByName(addLocationDto.getName());
        if (exists) throw new EntityExistsException("Location with name %s already exists".formatted(addLocationDto.getName()));

        Inventory inventory = inventoryRepository.findByUuid(addLocationDto.getInventoryUuid())
                .orElseThrow(() -> new EntityNotFoundException("Inventory with uuid %s not found"
                        .formatted(addLocationDto.getInventoryUuid())));

        Location location = build(addLocationDto, inventory);

        return locationRepository.save(location).getUuid();
    }

    @Override
    public void update(UpdateLocationDto updateLocationDto) {
        Location location = locationRepository.findByUuid(updateLocationDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Location with uuid %s not found"
                        .formatted(updateLocationDto.getUuid())));

        location.setName(updateLocationDto.getName());
        locationRepository.save(location);
    }

    private Location build(AddLocationDto addLocationDto, Inventory inventory) {
        return Location
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(addLocationDto.getName())
                .inventory(inventory)
                .build();
    }
}
