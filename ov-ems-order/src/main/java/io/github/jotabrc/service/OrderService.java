package io.github.jotabrc.service;

import io.github.jotabrc.dto.OrderCreationDto;
import io.github.jotabrc.dto.OrderDto;
import io.github.jotabrc.dto.OrderReturnItemDto;
import io.github.jotabrc.dto.OrderStatusUpdateDto;

import java.util.List;

public interface OrderService {

    String add(OrderCreationDto orderCreationDto);

    OrderDto getByOrderUuid(String uuid);

    List<OrderDto> getByUserUuid(String uuid);

    void updateOrderStatus(OrderStatusUpdateDto orderStatusUpdateDto);

    void cancelOrder(String uuid);

    void returnItem(OrderReturnItemDto orderReturnItemDto);
}
