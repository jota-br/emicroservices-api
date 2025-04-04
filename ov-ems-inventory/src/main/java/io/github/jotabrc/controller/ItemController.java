package io.github.jotabrc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.github.jotabrc.dto.*;
import io.github.jotabrc.response.ResponseBody;
import io.github.jotabrc.response.ResponsePayload;
import io.github.jotabrc.service.ItemService;

import java.net.URI;
import java.util.List;

import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_PREFIX;
import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/item")
@RestController
@Tag(name = "Item Controller", description = "Manage Item operations")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<ItemDto>> add(@RequestBody final AddItemDto addItemDto) {
        String uuid = itemService.add(addItemDto);
        URI location = ServletUriComponentsBuilder
                .fromPath(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/item/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<ItemDto>()
                .setMessage("Item inserted with uuid %s".formatted(uuid)));
    }

    @GetMapping("/get/uuid/{uuid}")
    public ResponseEntity<ResponsePayload<ItemDto>> getByUuid(@RequestBody final String uuid) {
        ItemDto itemDto = itemService.getByUuid(uuid);
        return ResponseEntity.status(HttpStatus.FOUND).body(new ResponsePayload<ItemDto>()
                .setMessage("Item with uuid %s found".formatted(uuid))
                .setBody(new ResponseBody<ItemDto>()
                        .setData(List.of(itemDto))));
    }

    @PutMapping("/update/stock")
    public ResponseEntity<ResponsePayload<ItemDto>> update(@RequestBody final UpdateProductStockDto updateProductStockDto) {
        itemService.updateStock(updateProductStockDto);
        return ResponseEntity.ok(new ResponsePayload<ItemDto>()
                .setMessage("Item with uuid %s stock has been updated".formatted(updateProductStockDto.getProductUuid())));
    }

    @PutMapping("/update/name")
    public ResponseEntity<ResponsePayload<InventoryDto>> updateName(@RequestBody final UpdateProductNameDto updateProductNameDto) {
        itemService.updateName(updateProductNameDto);
        return ResponseEntity.ok(new ResponsePayload<InventoryDto>()
                .setMessage("Item with uuid %s name has been updated".formatted(updateProductNameDto.getProductUuid())));
    }

    @PutMapping("/update/reserve")
    public ResponseEntity<ResponsePayload<InventoryDto>> updateReserve(@RequestBody final UpdateProductNameDto updateProductNameDto) {
        itemService.updateName(updateProductNameDto);
        return ResponseEntity.ok(new ResponsePayload<InventoryDto>()
                .setMessage("Item with uuid %s reserve has been updated".formatted(updateProductNameDto.getProductUuid())));
    }
}
