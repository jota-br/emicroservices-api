package io.github.jotabrc.util;

import io.github.jotabrc.dto.InventoryDto;
import io.github.jotabrc.dto.ItemDto;
import io.github.jotabrc.dto.LocationDto;
import io.github.jotabrc.model.Inventory;
import io.github.jotabrc.model.Item;
import io.github.jotabrc.model.Location;

public class ToDto {

    public static InventoryDto toDto(Inventory inventory) {
        return InventoryDto
                .builder()
                .uuid(inventory.getUuid())
                .name(inventory.getName())
                .locations(inventory.getLocations()
                        .stream()
                        .map(ToDto::toDto)
                        .toList())
                .build();
    }

    public static LocationDto toDto(Location location) {
        return LocationDto
                .builder()
                .uuid(location.getUuid())
                .name(location.getName())
                .inventoryId(location.getInventory().getId())
//                .items(location.getItems()
//                        .stream()
//                        .map(ToDto::toDto)
//                        .toList())
                .build();
    }

    public static ItemDto toDto(Item item) {
        return ItemDto
                .builder()
                .uuid(item.getUuid())
                .stock(item.getStock())
                .reserved(item.getReserved())
                .build();
    }
}
