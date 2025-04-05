package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class ProductPriceDto {

    @ValidateField(fieldName = "uuid", message = "Invalid UUID")
    private final String uuid;
    private final BigDecimal price;
}
