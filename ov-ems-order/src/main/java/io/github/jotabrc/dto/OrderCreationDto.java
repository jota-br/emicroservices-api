package io.github.jotabrc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import io.github.jotabrc.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class OrderCreationDto {

    private final String uuid;
    private final String userUuid;
    private final String userEmail;
    private final String username;
    private final String shippingAddress;
    private final String billingAddress;
    private final LocalDateTime orderDate;
    private final BigDecimal totalAmount;
    private final LocalDateTime updatedAt;
    private final OrderStatus status;
    private final List<OrderDetailDto> orderDetails;

}
