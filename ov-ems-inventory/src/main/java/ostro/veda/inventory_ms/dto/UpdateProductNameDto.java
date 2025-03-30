package ostro.veda.inventory_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class UpdateProductNameDto {

    private final String locationUuid;
    private final String productUuid;
    private final String productName;
}
