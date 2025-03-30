package ostro.veda.inventory_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class ItemDto {

    private final String uuid;
    private final String productUuid;
    private final String productName;
    private final int stock;
    private final int reserved;
    private final long locationId;
}
