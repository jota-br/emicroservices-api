package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddProductDto;
import io.github.jotabrc.dto.ProductDto;
import io.github.jotabrc.dto.ProductPriceDto;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ProductService {

    ProductDto getByName(@Valid @ValidateField(fieldName = "name", message = "Invalid Name") String name);

    List<ProductDto> getByCategories(@Valid @ValidateField(fieldName = "name", message = "Invalid Category Name") String category);

    ProductDto getByUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);

    String add(@Valid AddProductDto addProductDto);

    void update(@Valid ProductDto productDto);

    void updatePrice(@Valid ProductPriceDto productPriceDto);
}
