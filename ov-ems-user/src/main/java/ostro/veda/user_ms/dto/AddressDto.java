package ostro.veda.user_ms.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ostro.veda.user_ms.model.AddressType;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class AddressDto {

    private final String uuid;
    private final String postalCode;
    private final String street;
    private final String number;
    private final AddressType type;
    private final CityDto city;
}
