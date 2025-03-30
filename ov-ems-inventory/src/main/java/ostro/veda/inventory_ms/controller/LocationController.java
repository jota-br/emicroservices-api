package ostro.veda.inventory_ms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ostro.veda.inventory_ms.dto.AddLocationDto;
import ostro.veda.inventory_ms.dto.LocationDto;
import ostro.veda.inventory_ms.dto.UpdateLocationDto;
import ostro.veda.inventory_ms.response.ResponseBody;
import ostro.veda.inventory_ms.response.ResponsePayload;
import ostro.veda.inventory_ms.service.LocationService;

import java.net.URI;
import java.util.List;

import static ostro.veda.inventory_ms.controller.ControllerDefaults.MAPPING_PREFIX;
import static ostro.veda.inventory_ms.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/location")
@RestController
@Tag(name = "Location Controller", description = "Manage Location operations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<LocationDto>> add(@RequestBody final AddLocationDto addLocationDto) {
        String uuid = locationService.add(addLocationDto);
        URI location = ServletUriComponentsBuilder
                .fromPath(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/location/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<LocationDto>()
                .setMessage("Location inserted with uuid %s".formatted(uuid)));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponsePayload<LocationDto>> getByUuid(@RequestBody final String uuid) {
        LocationDto locationDto = locationService.getByUuid(uuid);
        return ResponseEntity.status(HttpStatus.FOUND).body(new ResponsePayload<LocationDto>()
                .setMessage("Location with uuid %s found".formatted(uuid))
                .setBody(new ResponseBody<LocationDto>()
                        .setData(List.of(locationDto))));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponsePayload<LocationDto>> update(@RequestBody final UpdateLocationDto updateLocationDto) {
        locationService.update(updateLocationDto);
        return ResponseEntity.ok(new ResponsePayload<LocationDto>()
                .setMessage("Location with uuid %s has been updated".formatted(updateLocationDto.getUuid())));
    }
}
