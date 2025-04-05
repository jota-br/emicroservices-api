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
public class UpdateProductStockDto {

    @ValidateField(fieldName = "uuid", message = "Invalid UUID")
    private final String locationUuid;

    @ValidateField(fieldName = "uuid", message = "Invalid UUID")
    private final String productUuid;
    private final int quantity;
}
