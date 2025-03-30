package ostro.veda.inventory_ms.service;

import ostro.veda.inventory_ms.dto.AddItemDto;
import ostro.veda.inventory_ms.dto.UpdateProductNameDto;
import ostro.veda.inventory_ms.dto.UpdateProductStockDto;

public interface ItemService {

    String add(AddItemDto addItemDto);

    void updateStock(UpdateProductStockDto updateProductStockDto);

    void updateName(UpdateProductNameDto updateProductNameDto);
}
