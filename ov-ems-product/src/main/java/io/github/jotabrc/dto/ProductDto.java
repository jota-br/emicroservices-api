package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductDto extends AbsProductDto {

    public ProductDto(String name, String description, BigDecimal price, boolean isActive, List<CategoryDto> categories, List<ImageDto> images, String uuid) {
        super(name, description, price, isActive, categories, images);
        this.uuid = uuid;
    }

    @ValidateField(fieldName = "uuid", message = "Invalid UUID")
    private final String uuid;
}
