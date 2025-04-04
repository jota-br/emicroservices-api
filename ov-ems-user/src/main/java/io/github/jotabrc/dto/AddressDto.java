package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.Accessors;
import io.github.jotabrc.model.AddressType;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class AddressDto {

    private final String uuid;

    @ValidateField(fieldName = "text50", message = "Invalid Street")
    private final String postalCode;

    @ValidateField(fieldName = "text255", message = "Invalid Street")
    private final String street;

    @ValidateField(fieldName = "text50", message = "Invalid username")
    private final String number;
    private final AddressType type;

    @Valid
    private final CityDto city;
}
