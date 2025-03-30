package ostro.veda.inventory_ms.service;

import ostro.veda.inventory_ms.dto.AddInventoryDto;
import ostro.veda.inventory_ms.dto.InventoryDto;

public interface InventoryService {

    String addInventory(AddInventoryDto addInventoryDto);

    InventoryDto getByUuid(String uuid);
}
