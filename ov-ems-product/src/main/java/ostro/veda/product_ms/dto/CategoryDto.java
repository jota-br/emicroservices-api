package ostro.veda.product_ms.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class CategoryDto {

    private final String name;
    private final String description;
    private final boolean isActive;
}
