package ostro.veda.user_ms.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class CountryDto {

    private final String uuid;
    private final String name;
}
