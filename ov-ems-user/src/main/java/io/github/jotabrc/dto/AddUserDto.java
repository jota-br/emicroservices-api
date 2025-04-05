package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateEmail;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class AddUserDto {
    @ValidateField(fieldName = "username", message = "Invalid Username")
    private final String username;

    @ValidateEmail(message = "Invalid Email")
    private final String email;

    @ValidateField(fieldName = "password", message = "Invalid Password")
    private final String password;

    @ValidateField(fieldName = "name", message = "Invalid First name")
    private final String firstName;

    @ValidateField(fieldName = "name", message = "Invalid Last name")
    private final String lastName;

    @ValidateField(fieldName = "phone", message = "Invalid Phone")
    private final String phone;
}
