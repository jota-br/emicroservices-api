package io.github.jotabrc.service;

import io.github.jotabrc.dto.*;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ItemService {

    String add(@Valid AddItemDto addItemDto);

    ItemDto getByUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);

    void updateStock(@Valid UpdateProductStockDto updateProductStockDto);

    void updateName(@Valid UpdateProductNameDto updateProductNameDto);

    void updateReserve(AddOrderDto addOrderDto);

    boolean updateReserve(@Valid UpdateProductStockDto updateProductStockDto);
}
