package io.github.jotabrc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.dto.*;
import io.github.jotabrc.model.Order;
import io.github.jotabrc.model.OrderDetail;
import io.github.jotabrc.model.OrderStatus;
import io.github.jotabrc.model.OrderStatusHistory;
import io.github.jotabrc.ov_auth_validator.authorization.UsernameAuthorizationValidator;
import io.github.jotabrc.ov_kafka_cp.TopicConstant;
import io.github.jotabrc.ov_kafka_cp.broker.Producer;
import io.github.jotabrc.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class OrderServiceImpl implements OrderService {

    private final UsernameAuthorizationValidator usernameAuthorizationValidator;
    private final OrderRepository orderRepository;
    private final Producer producer;

    public OrderServiceImpl(UsernameAuthorizationValidator usernameAuthorizationValidator, OrderRepository orderRepository, Producer producer) {
        this.usernameAuthorizationValidator = usernameAuthorizationValidator;
        this.orderRepository = orderRepository;
        this.producer = producer;
    }

    @Override
    public String add(final OrderCreationDto orderCreationDto) {
        usernameAuthorizationValidator.validate(orderCreationDto.getUsername());
        Order order = build(orderCreationDto);
        order = orderRepository.save(order);

        callProducer(order, TopicConstant.INVENTORY_ADD_ORDER);
        return order.getUuid();
    }

    @Override
    public OrderDto getByOrderUuid(final String uuid) {
        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Order with uuid %s not found".formatted(uuid)));

        usernameAuthorizationValidator.validate(order.getUsername());
        return toDto(order);
    }

    @Override
    public List<OrderDto> getByUserUuid(final String uuid) {
        List<Order> orders = orderRepository.findByUserUuid(uuid);

        if (orders.isEmpty()) throw new EntityNotFoundException("No Order found with user uuid %s".formatted(uuid));

        orders.forEach(order -> usernameAuthorizationValidator.validate(order.getUsername()));

        return orders.stream().map(this::toDto).toList();
    }

    @Override
    public void updateOrderStatus(final OrderStatusUpdateDto orderStatusUpdateDto) {
        Order order = orderRepository.findByUuid(orderStatusUpdateDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Order with uuid %s not found".formatted(orderStatusUpdateDto.getUuid())));

        usernameAuthorizationValidator.validate(order.getUsername());

        updateOrderStatus(orderStatusUpdateDto.getStatus(), order);

        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(final String uuid) {
        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Order with uuid %s not found".formatted(uuid)));

        usernameAuthorizationValidator.validate(order.getUsername());

        if (order.getOrderDate().plusDays(30).isAfter(LocalDateTime.now()))
            throw new IllegalStateException("Cancellation date exceeded");

        OrderStatus orderStatus = order.getStatus();

        if (
                !orderStatus.equals(OrderStatus.PENDING) &&
                        !orderStatus.equals(OrderStatus.PROCESSING) &&
                        !orderStatus.equals(OrderStatus.READY)
        )
            throw new IllegalStateException("Cancellation is unavailable for this order");

        updateOrderStatus(OrderStatus.CANCELLED, order);
        List<OrderDetail> orderDetailList = buildOrderDetailsToCancel(order.getOrderDetails());
        orderRepository.save(order);

        String topic = getTopic(orderStatus);
        orderDetailList = getDetailList(orderDetailList, orderStatus, order);
        callProducer(orderDetailList, topic);
    }

    @Override
    public void returnItem(final OrderReturnItemDto orderReturnItemDto) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Order order = orderRepository.findByUuid(orderReturnItemDto.getOrderUuid())
                .orElseThrow(() -> new EntityNotFoundException("Order with uuid %s not found".formatted(orderReturnItemDto.getOrderUuid())));

        usernameAuthorizationValidator.validate(order.getUsername());

        if (LocalDateTime.now().isBefore(order.getOrderDate().minusDays(30)))
            throw new IllegalStateException("Order cannot be returned");

        updateOrderStatus(OrderStatus.RETURN_REQUESTED, order);
        buildOrderDetailToReturn(orderReturnItemDto, order);
        orderRepository.save(order);
    }

    private Order build(final OrderCreationDto orderCreationDto) {
        Order order = Order
                .builder()
                .uuid(UUID.randomUUID().toString())
                .userUuid(orderCreationDto.getUserUuid())
                .username(orderCreationDto.getUsername())
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

    private List<OrderDetail> buildOrderDetailsToCancel(List<OrderDetail> orderDetails) {
        List<OrderDetail> orderDetailList = orderDetails
                .stream()
                .map(buildOrderDetailToCancel())
                .toList();
        orderDetails.addAll(orderDetailList);
        return orderDetailList;
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

    private void buildOrderDetailToReturn(OrderReturnItemDto orderReturnItemDto, Order order) {
        OrderDetail orderDetail = OrderDetail
                .builder()
                .uuid(UUID.randomUUID().toString())
                .productUuid(orderReturnItemDto.getProductUuid())
                .productName(orderReturnItemDto.getProductName())
                .quantity(-orderReturnItemDto.getQuantity())
                .unitPrice(orderReturnItemDto.getUnitPrice().subtract(orderReturnItemDto.getUnitPrice().multiply(BigDecimal.valueOf(2))))
                .order(order)
                .build();
        order.getOrderDetails().add(orderDetail);
    }

    private OrderDto toDto(Order order) {
        return OrderDto.builder()
                .uuid(UUID.randomUUID().toString())
                .userUuid(order.getUserUuid())
                .username(order.getUsername())
                .userEmail(order.getUserEmail())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDetails(order.getOrderDetails()
                        .stream()
                        .map(this::toDto)
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

    private OrderDetailDto toDto(OrderDetail orderDetail) {
        return OrderDetailDto
                .builder()
                .uuid(orderDetail.getUuid())
                .productUuid(orderDetail.getProductUuid())
                .productName(orderDetail.getProductName())
                .quantity(orderDetail.getQuantity())
                .unitPrice(orderDetail.getUnitPrice())
                .orderId(orderDetail.getOrder().getId())
                .build();
    }

    private List<OrderDetail> getDetailList(List<OrderDetail> orderDetailList, OrderStatus orderStatus, Order order) {
        orderDetailList = orderStatus.equals(OrderStatus.PENDING) || orderStatus.equals(OrderStatus.READY) ? orderDetailList : order.getOrderDetails();
        return orderDetailList;
    }

    private String getTopic(OrderStatus orderStatus) {
        String topic;
        if (orderStatus.equals(OrderStatus.PENDING) || orderStatus.equals(OrderStatus.PROCESSING))
            topic = TopicConstant.INVENTORY_CANCEL_RESERVED_ORDER;
        else topic = TopicConstant.INVENTORY_CANCEL_ORDER;
        return topic;
    }

    private void callProducer(Order order, String topic) {
        order.getOrderDetails().forEach(orderDetail -> callProducer(orderDetail, topic));
    }

    private void callProducer(List<OrderDetail> orderDetailList, String topic) {
        orderDetailList.forEach(orderDetail -> callProducer(orderDetail, topic));
    }

    private void callProducer(OrderDetail orderDetail, String topic) {
        try {
            producer.producer(toDto(orderDetail), "localhost:9092", topic);
        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
