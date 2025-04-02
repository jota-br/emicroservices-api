package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddLocationDto;
import io.github.jotabrc.dto.LocationDto;
import io.github.jotabrc.dto.UpdateLocationDto;

public interface LocationService {

    String add(AddLocationDto addLocationDto);

    LocationDto getByUuid(String uuid);

    void update(UpdateLocationDto updateLocationDto);
}
