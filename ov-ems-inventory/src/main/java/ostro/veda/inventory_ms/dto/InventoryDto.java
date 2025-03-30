package ostro.veda.inventory_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class InventoryDto {

    private final String uuid;
    private final String name;
    private final List<LocationDto> locations;
}

