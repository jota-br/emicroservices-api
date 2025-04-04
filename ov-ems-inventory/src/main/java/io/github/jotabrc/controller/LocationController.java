package io.github.jotabrc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.github.jotabrc.dto.AddLocationDto;
import io.github.jotabrc.dto.LocationDto;
import io.github.jotabrc.dto.UpdateLocationDto;
import io.github.jotabrc.response.ResponseBody;
import io.github.jotabrc.response.ResponsePayload;
import io.github.jotabrc.service.LocationService;

import java.net.URI;
import java.util.List;

import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_PREFIX;
import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

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

    @GetMapping("/get/uuid/{uuid}")
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
