package io.github.jotabrc.service;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;

public interface ActivationTokenService {

    void add(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String userUuid);

    String activate(@Valid @ValidateField(fieldName = "uuid", message = "Invalid Token") String token);
}
