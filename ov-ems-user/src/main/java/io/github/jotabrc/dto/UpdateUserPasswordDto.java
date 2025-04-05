package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class UpdateUserPasswordDto {

    @ValidateField(fieldName = "uuid", message = "Invalid UUID")
    private final String uuid;

    @ValidateField(fieldName = "password", message = "Invalid Password")
    private final String password;
}
