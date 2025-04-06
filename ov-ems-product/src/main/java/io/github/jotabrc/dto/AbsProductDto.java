package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class AbsProductDto {

    @ValidateField(fieldName = "name", message = "Invalid Name")
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
