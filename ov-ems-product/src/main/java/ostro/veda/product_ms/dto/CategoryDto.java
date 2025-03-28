package ostro.veda.product_ms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class CategoryDto {

    private String name;
    private String description;
    private boolean isActive;
}
