package ostro.veda.inventory_ms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ostro.veda.inventory_ms.dto.*;
import ostro.veda.inventory_ms.response.ResponsePayload;
import ostro.veda.inventory_ms.service.ItemService;

import java.net.URI;

import static ostro.veda.inventory_ms.controller.ControllerDefaults.MAPPING_PREFIX;
import static ostro.veda.inventory_ms.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

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
}
