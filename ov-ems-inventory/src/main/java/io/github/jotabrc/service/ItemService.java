package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddItemDto;
import io.github.jotabrc.dto.ItemDto;
import io.github.jotabrc.dto.UpdateProductNameDto;
import io.github.jotabrc.dto.UpdateProductStockDto;

public interface ItemService {

    String add(AddItemDto addItemDto);

    ItemDto getByUuid(String uuid);

    void updateStock(UpdateProductStockDto updateProductStockDto);

    void updateName(UpdateProductNameDto updateProductNameDto);

    void updateReserve(UpdateProductStockDto updateProductStockDto);
}
