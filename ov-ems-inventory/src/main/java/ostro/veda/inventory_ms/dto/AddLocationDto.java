package ostro.veda.inventory_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class AddLocationDto {

    private final String uuid;
    private final String name;
    private final String inventoryUuid;
}
