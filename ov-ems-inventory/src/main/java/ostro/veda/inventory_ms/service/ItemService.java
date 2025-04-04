package ostro.veda.inventory_ms.service;

import ostro.veda.inventory_ms.dto.AddItemDto;
import ostro.veda.inventory_ms.dto.ItemDto;
import ostro.veda.inventory_ms.dto.UpdateProductNameDto;
import ostro.veda.inventory_ms.dto.UpdateProductStockDto;

public interface ItemService {

    String add(AddItemDto addItemDto);

    ItemDto getByUuid(String uuid);

    void updateStock(UpdateProductStockDto updateProductStockDto);

    void updateName(UpdateProductNameDto updateProductNameDto);

    void updateReserve(UpdateProductStockDto updateProductStockDto);
}
