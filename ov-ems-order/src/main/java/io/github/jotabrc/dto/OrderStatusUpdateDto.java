package io.github.jotabrc.dto;

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

    private final String uuid;
    private final OrderStatus status;
}
