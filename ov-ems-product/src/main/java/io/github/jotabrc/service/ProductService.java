package io.github.jotabrc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.dto.AddProductDto;
import io.github.jotabrc.dto.ProductDto;
import io.github.jotabrc.dto.ProductPriceDto;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Validated
public interface ProductService {

    ProductDto getByName(@Valid @ValidateField(fieldName = "name", message = "Invalid Name") String name);

    List<ProductDto> getByCategories(@Valid @ValidateField(fieldName = "name", message = "Invalid Category Name") String category);

    ProductDto getByUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);

    String add(@Valid AddProductDto addProductDto) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;

    void update(@Valid ProductDto productDto) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;

    void updatePrice(@Valid ProductPriceDto productPriceDto);
}
