package ostro.veda.order_ms.service;

import ostro.veda.order_ms.dto.OrderCreationDto;
import ostro.veda.order_ms.dto.OrderDto;
import ostro.veda.order_ms.dto.OrderReturnItemDto;
import ostro.veda.order_ms.dto.OrderStatusUpdateDto;

import java.util.List;

public interface OrderService {

    String add(OrderCreationDto orderCreationDto);

    OrderDto getByOrderUuid(String uuid);

    List<OrderDto> getByUserUuid(String uuid);

    void updateOrderStatus(OrderStatusUpdateDto orderStatusUpdateDto);

    void cancelOrder(String uuid);

    void returnItem(OrderReturnItemDto orderReturnItemDto);
}
