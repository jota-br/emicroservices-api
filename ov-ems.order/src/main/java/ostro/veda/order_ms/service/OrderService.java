package ostro.veda.order_ms.service;

import ostro.veda.order_ms.dto.OrderCreationDto;
import ostro.veda.order_ms.model.Order;

public interface OrderService {

    String add(OrderCreationDto orderCreationDto);

    Order build(OrderCreationDto orderCreationDto);
}
