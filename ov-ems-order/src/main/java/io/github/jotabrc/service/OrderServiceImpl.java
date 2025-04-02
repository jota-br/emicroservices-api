package io.github.jotabrc.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import io.github.jotabrc.dto.*;
import io.github.jotabrc.model.Order;
import io.github.jotabrc.model.OrderDetail;
import io.github.jotabrc.model.OrderStatus;
import io.github.jotabrc.model.OrderStatusHistory;
import io.github.jotabrc.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

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
    public OrderDto getByOrderUuid(String uuid) {
        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Order with uuid %s not found".formatted(uuid)));
        return toDto(order);
    }

    @Override
    public List<OrderDto> getByUserUuid(String uuid) {
        List<Order> orders = orderRepository.findByUserUuid(uuid);

        if(orders.isEmpty()) throw  new EntityNotFoundException("No Order found with user uuid %s".formatted(uuid));

        return orders.stream().map(this::toDto).toList();
    }

    @Override
    public void updateOrderStatus(final OrderStatusUpdateDto orderStatusUpdateDto) {
        Order order = orderRepository.findByUuid(orderStatusUpdateDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Order with uuid %s not found".formatted(orderStatusUpdateDto.getUuid())));

        updateOrderStatus(orderStatusUpdateDto.getStatus(), order);

        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(String uuid) {
        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Order with uuid %s not found".formatted(uuid)));

        updateOrderStatus(OrderStatus.CANCELLED, order);
        buildOrderDetailsToCancel(order.getOrderDetails());
        orderRepository.save(order);
    }

    @Override
    public void returnItem(OrderReturnItemDto orderReturnItemDto) {
        Order order = orderRepository.findByUuid(orderReturnItemDto.getOrderUuid())
                .orElseThrow(() -> new EntityNotFoundException("Order with uuid %s not found".formatted(orderReturnItemDto.getOrderUuid())));

        if (LocalDateTime.now().isBefore(order.getOrderDate().minusDays(30)))
            throw new IllegalStateException("Order cannot be returned");

        updateOrderStatus(OrderStatus.RETURN_REQUESTED, order);
        order.getOrderDetails().add(
                buildOrderDetailToReturn(orderReturnItemDto, order)
        );

        orderRepository.save(order);
    }

    private Order build(final OrderCreationDto orderCreationDto) {
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
                        .productUuid(orderDetailDto.getProductUuid())
                        .productName(orderDetailDto.getProductName())
                        .quantity(orderDetailDto.getQuantity())
                        .unitPrice(orderDetailDto.getUnitPrice())
                        .order(order)
                        .build()
                )
                .toList();

        List<OrderStatusHistory> orderStatusHistories = (List.of(
                buildOrderStatusHistory(order.getStatus(), order)
        ));

        return order
                .setOrderDetails(orderDetails)
                .setOrderStatusHistories(orderStatusHistories);
    }

    private void updateOrderStatus(OrderStatus orderStatus, Order order) {
        order.setStatus(orderStatus);
        OrderStatusHistory orderStatusHistory = buildOrderStatusHistory(orderStatus, order);
        order.getOrderStatusHistories().add(orderStatusHistory);
    }

    private OrderStatusHistory buildOrderStatusHistory(OrderStatus orderStatus, Order order) {
        return OrderStatusHistory
                .builder()
                .uuid(UUID.randomUUID().toString())
                .orderStatus(orderStatus)
                .order(order)
                .build();
    }

    private void buildOrderDetailsToCancel(List<OrderDetail> orderDetails) {
        orderDetails.addAll(
                orderDetails
                        .stream()
                        .map(buildOrderDetailToCancel())
                        .toList()
        );
    }

    private Function<OrderDetail, OrderDetail> buildOrderDetailToCancel() {
        return orderDetail -> OrderDetail
                .builder()
                .uuid(UUID.randomUUID().toString())
                .productUuid(orderDetail.getProductUuid())
                .productName(orderDetail.getProductName())
                .quantity(-orderDetail.getQuantity())
                .unitPrice(orderDetail.getUnitPrice().subtract(orderDetail.getUnitPrice().multiply(BigDecimal.valueOf(2))))
                .order(orderDetail.getOrder())
                .build();
    }

    private OrderDetail buildOrderDetailToReturn(OrderReturnItemDto orderReturnItemDto, Order order) {
        return OrderDetail
                .builder()
                .uuid(UUID.randomUUID().toString())
                .productUuid(orderReturnItemDto.getProductUuid())
                .productName(orderReturnItemDto.getProductName())
                .quantity(-orderReturnItemDto.getQuantity())
                .unitPrice(orderReturnItemDto.getUnitPrice().subtract(orderReturnItemDto.getUnitPrice().multiply(BigDecimal.valueOf(2))))
                .order(order)
                .build();
    }

    private OrderDto toDto(Order order) {
        return OrderDto.builder()
                .uuid(UUID.randomUUID().toString())
                .userUuid(order.getUserUuid())
                .userEmail(order.getUserEmail())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDetails(order.getOrderDetails()
                        .stream()
                        .map(orderDetail -> OrderDetailDto
                                .builder()
                                .uuid(order.getUuid())
                                .productUuid(orderDetail.getProductUuid())
                                .productName(orderDetail.getProductName())
                                .quantity(orderDetail.getQuantity())
                                .unitPrice(orderDetail.getUnitPrice())
                                .orderId(order.getId())
                                .build())
                        .toList())
                .orderStatusHistories(order.getOrderStatusHistories()
                        .stream()
                        .map(orderStatusHistory -> OrderStatusHistoryDto
                                .builder()
                                .uuid(orderStatusHistory.getUuid())
                                .orderStatus(orderStatusHistory.getOrderStatus())
                                .orderId(order.getId())
                                .build())
                        .toList())
                .build();
    }
}
