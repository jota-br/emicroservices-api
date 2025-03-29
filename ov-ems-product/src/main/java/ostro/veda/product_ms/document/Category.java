package ostro.veda.product_ms.document;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class Category {

    private String name;
    private String description;
    private boolean isActive;
}
