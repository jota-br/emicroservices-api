package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class AddItemDto {

    private final String uuid;

    @ValidateField(fieldName = "uuid", message = "Invalid UUID")
    private final String productUuid;
    private final String productName;
    private final int stock;
    private final int reserved;
    private final String locationUuid;
}
