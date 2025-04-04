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
public class LoginDto {

    @ValidateField(fieldName = "username", message = "Invalid Username")
    private final String username;

    @ValidateField(fieldName = "password", message = "Invalid Password")
    private final String password;
}
