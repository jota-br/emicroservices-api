package ostro.veda.inventory_ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ostro.veda.inventory_ms.dto.InventoryDto;
import ostro.veda.inventory_ms.response.ResponsePayload;
import ostro.veda.inventory_ms.service.InventoryService;

import java.net.URI;

import static ostro.veda.inventory_ms.controller.ControllerDefaults.MAPPING_PREFIX;
import static ostro.veda.inventory_ms.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/inventory")
@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<InventoryDto>> addInventory(@RequestBody final InventoryDto inventoryDto) {
        String uuid = inventoryService.addInventory(inventoryDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<InventoryDto>()
                .setMessage("Inventory inserted with uuid %s".formatted(uuid)));
    }
}
