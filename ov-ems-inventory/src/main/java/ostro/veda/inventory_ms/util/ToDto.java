package ostro.veda.inventory_ms.util;

import ostro.veda.inventory_ms.dto.InventoryDto;
import ostro.veda.inventory_ms.dto.ItemDto;
import ostro.veda.inventory_ms.dto.LocationDto;
import ostro.veda.inventory_ms.model.Inventory;
import ostro.veda.inventory_ms.model.Item;
import ostro.veda.inventory_ms.model.Location;

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
                .items(location.getItems()
                        .stream()
                        .map(ToDto::toDto)
                        .toList())
                .build();
    }

    public static ItemDto toDto(Item item) {
        return ItemDto
                .builder()
                .uuid(item.getUuid())
                .productUuid(item.getProductUuid())
                .productName(item.getProductName())
                .stock(item.getStock())
                .reserved(item.getReserved())
                .locationId(item.getLocation().getId())
                .build();
    }
}
