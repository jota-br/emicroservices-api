package io.github.jotabrc.service;

import io.github.jotabrc.dto.ProductDto;
import io.github.jotabrc.dto.ProductPriceDto;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ProductService {

    ProductDto getByName(@ValidateField(fieldName = "text255", message = "Invalid Name") String name);

    List<ProductDto> getByCategories(@ValidateField(fieldName = "text255", message = "Invalid Category Name") String category);

    ProductDto getByUuid(String uuid);

    String add(@Valid ProductDto productDto);

    void update(@Valid ProductDto productDto);

    void updatePrice(ProductPriceDto productPriceDto);
}
