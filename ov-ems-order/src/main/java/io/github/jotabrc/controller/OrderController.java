package io.github.jotabrc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.dto.OrderCreationDto;
import io.github.jotabrc.dto.OrderDto;
import io.github.jotabrc.dto.OrderReturnItemDto;
import io.github.jotabrc.dto.OrderStatusUpdateDto;
import io.github.jotabrc.response.ResponseBody;
import io.github.jotabrc.response.ResponsePayload;
import io.github.jotabrc.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_PREFIX;
import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/order")
@RestController
@Tag(name = "Order Controller", description = "Manage Order operations")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<OrderDto>> add(@RequestBody final OrderCreationDto orderCreationDto) {
        String uuid = orderService.add(orderCreationDto);
        URI location = ServletUriComponentsBuilder
                .fromPath(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/order/uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<OrderDto>()
                .setMessage("Order inserted with uuid %s".formatted(uuid)));
    }

    @GetMapping("/get/uuid/{uuid}")
    public ResponseEntity<ResponsePayload<OrderDto>> getByOrderUuid(@PathVariable("uuid") final String uuid) {
        OrderDto orderDto = orderService.getByOrderUuid(uuid);
        return ResponseEntity.ok(new ResponsePayload<OrderDto>()
                .setMessage("Order found")
                .setBody(new ResponseBody<OrderDto>()
                        .setData(List.of(orderDto))));
    }

    @GetMapping("/get/user/uuid/{uuid}")
    public ResponseEntity<ResponsePayload<OrderDto>> getByUserUuid(@PathVariable("uuid") final String uuid) {
        List<OrderDto> orderDtos = orderService.getByUserUuid(uuid);
        return ResponseEntity.ok(new ResponsePayload<OrderDto>()
                .setMessage("Order found")
                .setBody(new ResponseBody<OrderDto>()
                        .setData(orderDtos)));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponsePayload<OrderDto>> update(@RequestBody final OrderStatusUpdateDto orderStatusUpdateDto) {
        orderService.updateOrderStatus(orderStatusUpdateDto);
        return ResponseEntity.ok(new ResponsePayload<OrderDto>()
                .setMessage("Order Status updated to %s".formatted(orderStatusUpdateDto.getStatus())));
    }

    @PostMapping("/cancel/uuid/{uuid}")
    public ResponseEntity<ResponsePayload<OrderDto>> cancel(@PathVariable("uuid") final String uuid) {
        orderService.cancelOrder(uuid);
        return ResponseEntity.ok(new ResponsePayload<OrderDto>()
                .setMessage("Order with uuid %s has been CANCELLED".formatted(uuid)));
    }

    @PostMapping("/return")
    public ResponseEntity<ResponsePayload<OrderDto>> returnItem(@RequestBody final OrderReturnItemDto orderReturnItemDto) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        orderService.returnItem(orderReturnItemDto);
        return ResponseEntity.ok(new ResponsePayload<OrderDto>()
                .setMessage("Product with uuid %s return requested".formatted(orderReturnItemDto.getProductUuid())));
    }
}
