package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddInventoryDto;
import io.github.jotabrc.dto.InventoryDto;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface InventoryService {

    String addInventory(@Valid AddInventoryDto addInventoryDto);

    InventoryDto getByUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);
}
