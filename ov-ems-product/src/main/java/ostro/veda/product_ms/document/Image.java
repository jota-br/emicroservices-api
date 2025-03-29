package ostro.veda.product_ms.document;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class Image {

    private String imagePath;
    private boolean isMain;
}
