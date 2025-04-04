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
public class UpdateUserDto {

    private final String uuid;

    @ValidateField(fieldName = "username", message = "Invalid Username")
    private final String username;

    @ValidateEmail(message = "Invalid Email")
    private final String email;

    @ValidateField(fieldName = "text255", message = "Invalid First Name")
    private final String firstName;

    @ValidateField(fieldName = "text255", message = "Invalid Last Name")
    private final String lastName;

    @ValidateField(fieldName = "text255", message = "Invalid Phone")
    private final String phone;
}
