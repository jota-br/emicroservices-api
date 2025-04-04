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
public class OrderReturnItemDto {

    private final String orderUuid;
    private final String productUuid;

    @ValidateField(fieldName = "name", message = "Invalid Product Name")
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;

}
