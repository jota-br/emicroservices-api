package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class ProductDto {

    private final String uuid;

    @ValidateField(fieldName = "text255", message = "Invalid Name")
    private final String name;

    @ValidateField(fieldName = "description", message = "Invalid Description")
    private final String description;
    private final BigDecimal price;
    private final boolean isActive;

    @Valid
    private final List<CategoryDto> categories;

    @Valid
    private final List<ImageDto> images;
}
