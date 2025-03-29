package ostro.veda.order_ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ostro.veda.order_ms.dto.OrderCreationDto;
import ostro.veda.order_ms.dto.OrderDto;
import ostro.veda.order_ms.response.ResponsePayload;
import ostro.veda.order_ms.service.OrderService;

import java.net.URI;

import static ostro.veda.order_ms.controller.ControllerDefaults.MAPPING_PREFIX;
import static ostro.veda.order_ms.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/order")
@RestController
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
                .fromCurrentRequest()
                .path("/uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<OrderDto>()
                .setMessage("Order inserted with uuid %s".formatted(uuid)));
    }
}
