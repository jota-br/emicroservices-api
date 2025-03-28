package ostro.veda.product_ms.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class ImageDto {

    private final String imagePath;
    private final boolean isMain;
}
