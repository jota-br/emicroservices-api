package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class CategoryDto {

    @ValidateField(fieldName = "text255", message = "Invalid Name")
    private final String name;

    @ValidateField(fieldName = "description", message = "Invalid Description")
    private final String description;
    private final boolean isActive;
}
