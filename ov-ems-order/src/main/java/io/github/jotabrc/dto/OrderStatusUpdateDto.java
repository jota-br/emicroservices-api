package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import io.github.jotabrc.model.OrderStatus;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class OrderStatusUpdateDto {

    @ValidateField(fieldName = "uuid", message = "Invalid UUID")
    private final String uuid;
    private final OrderStatus status;
}
