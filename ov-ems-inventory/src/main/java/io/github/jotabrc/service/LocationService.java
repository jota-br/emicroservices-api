package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddLocationDto;
import io.github.jotabrc.dto.LocationDto;
import io.github.jotabrc.dto.UpdateLocationDto;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface LocationService {

    String add(@Valid AddLocationDto addLocationDto);

    LocationDto getByUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);

    void update(@Valid UpdateLocationDto updateLocationDto);
}
