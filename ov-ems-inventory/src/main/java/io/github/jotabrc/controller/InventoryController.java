package io.github.jotabrc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.github.jotabrc.dto.AddInventoryDto;
import io.github.jotabrc.dto.InventoryDto;
import io.github.jotabrc.response.ResponseBody;
import io.github.jotabrc.response.ResponsePayload;
import io.github.jotabrc.service.InventoryService;

import java.net.URI;
import java.util.List;

import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_PREFIX;
import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/inventory")
@RestController
@Tag(name = "Inventory Controller", description = "Creates Inventory unit")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<InventoryDto>> addInventory(@RequestBody final AddInventoryDto addInventoryDto) {
        String uuid = inventoryService.addInventory(addInventoryDto);
        URI location = ServletUriComponentsBuilder
                .fromPath(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/inventory/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<InventoryDto>()
                .setMessage("Inventory inserted with uuid %s".formatted(uuid)));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponsePayload<InventoryDto>> getByUuid(@RequestBody final String uuid) {
        InventoryDto inventoryDto = inventoryService.getByUuid(uuid);
        return ResponseEntity.status(HttpStatus.FOUND).body(new ResponsePayload<InventoryDto>()
                .setMessage("Inventory with uuid %s found".formatted(uuid))
                .setBody(new ResponseBody<InventoryDto>()
                        .setData(List.of(inventoryDto))));
    }
}
