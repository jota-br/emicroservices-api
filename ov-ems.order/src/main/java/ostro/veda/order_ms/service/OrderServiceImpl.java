package ostro.veda.order_ms.service;

import org.springframework.stereotype.Service;
import ostro.veda.order_ms.dto.OrderCreationDto;
import ostro.veda.order_ms.model.Order;
import ostro.veda.order_ms.model.OrderDetail;
import ostro.veda.order_ms.model.OrderStatusHistory;
import ostro.veda.order_ms.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public String add(final OrderCreationDto orderCreationDto) {
        Order order = build(orderCreationDto);
        return orderRepository.save(order).getUuid();
    }

    @Override
    public Order build(final OrderCreationDto orderCreationDto) {
        Order order = Order
                .builder()
                .uuid(UUID.randomUUID().toString())
                .userUuid(orderCreationDto.getUserUuid())
                .userEmail(orderCreationDto.getUserEmail())
                .shippingAddress(orderCreationDto.getShippingAddress())
                .billingAddress(orderCreationDto.getBillingAddress())
                .totalAmount(orderCreationDto.getTotalAmount())
                .status(orderCreationDto.getStatus())
                .build();

        List<OrderDetail> orderDetails = orderCreationDto.getOrderDetails().stream()
                .map(orderDetailDto -> OrderDetail
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .productUuid(orderDetailDto.getProduct_uuid())
                        .productName(orderDetailDto.getProductName())
                        .quantity(orderDetailDto.getQuantity())
                        .unitPrice(orderDetailDto.getUnitPrice())
                        .order(order)
                        .build()
                )
                .toList();

        List<OrderStatusHistory> orderStatusHistories = (List.of(
                OrderStatusHistory
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .orderStatus(orderCreationDto.getStatus())
                        .order(order)
                        .build()
        ));

        return order
                .setOrderDetails(orderDetails)
                .setOrderStatusHistories(orderStatusHistories);
    }
}
