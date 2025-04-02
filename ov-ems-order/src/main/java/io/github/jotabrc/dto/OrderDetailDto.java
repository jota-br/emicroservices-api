package io.github.jotabrc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class OrderDetailDto {

    private final String uuid;
    private final long orderId;
    private final String productUuid;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;
}
