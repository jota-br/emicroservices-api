package io.github.jotabrc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class OrderToInventoryDto {

    private final String orderUuid;
    private final String productUuid;
    private final int quantity;
}
