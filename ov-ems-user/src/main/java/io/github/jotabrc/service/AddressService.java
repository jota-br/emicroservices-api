package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddressDto;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AddressService {

    AddressDto getAddressByUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);
}
