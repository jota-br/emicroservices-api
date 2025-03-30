package ostro.veda.inventory_ms.service;

import ostro.veda.inventory_ms.dto.AddLocationDto;
import ostro.veda.inventory_ms.dto.UpdateLocationDto;

public interface LocationService {

    String add(AddLocationDto addLocationDto);

    void update(UpdateLocationDto updateLocationDto);
}
