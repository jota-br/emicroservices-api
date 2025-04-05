package io.github.jotabrc.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import io.github.jotabrc.dto.AddLocationDto;
import io.github.jotabrc.dto.LocationDto;
import io.github.jotabrc.dto.UpdateLocationDto;
import io.github.jotabrc.model.Inventory;
import io.github.jotabrc.model.Location;
import io.github.jotabrc.repository.InventoryRepository;
import io.github.jotabrc.repository.LocationRepository;

import java.util.UUID;

import static io.github.jotabrc.util.ToDto.toDto;

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
    public LocationDto getByUuid(final String uuid) {
        Location location = locationRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Location with uuid %s not found"
                        .formatted(uuid)));

        return toDto(location);
    }

    @Override
    public void update(final UpdateLocationDto updateLocationDto) {
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
