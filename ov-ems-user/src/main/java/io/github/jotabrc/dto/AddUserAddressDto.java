package io.github.jotabrc.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class AddUserAddressDto {

    private final String uuid;

    @Valid
    private final AddressDto address;
}
