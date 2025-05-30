package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class CountryDto {

    private final String uuid;

    @ValidateField(fieldName = "text255", message = "Invalid name")
    private final String name;
}
