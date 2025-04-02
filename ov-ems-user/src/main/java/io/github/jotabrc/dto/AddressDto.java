package io.github.jotabrc.dto;

import lombok.*;
import lombok.experimental.Accessors;
import io.github.jotabrc.model.AddressType;

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
