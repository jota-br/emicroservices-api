package ostro.veda.product_ms.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Category {

    private String name;
    private String description;
    private boolean isActive;
}
