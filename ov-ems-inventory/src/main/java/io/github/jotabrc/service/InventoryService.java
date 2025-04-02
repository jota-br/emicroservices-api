package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddInventoryDto;
import io.github.jotabrc.dto.InventoryDto;

public interface InventoryService {

    String addInventory(AddInventoryDto addInventoryDto);

    InventoryDto getByUuid(String uuid);
}
