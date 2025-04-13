package io.github.jotabrc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.dto.*;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Validated
public interface ItemService {

    String add(@Valid AddItemDto addItemDto);

    ItemDto getByUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);

    void updateStock(@Valid UpdateProductStockDto updateProductStockDto);

    void updateName(@Valid UpdateProductNameDto updateProductNameDto);

    void updateReserve(AddOrderDto addOrderDto) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;

    boolean updateReserve(@Valid UpdateProductStockDto updateProductStockDto);
}
