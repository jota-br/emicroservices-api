package io.github.jotabrc.service;

import io.github.jotabrc.dto.*;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.security.NoSuchAlgorithmException;

@Validated
public interface UserService {

    String add(@Valid AddUserDto addUserDto) throws NoSuchAlgorithmException;

    void update(@Valid UpdateUserDto updateUserDto);

    void updatePassword(@Valid UpdateUserPasswordDto updateUserPasswordDto) throws NoSuchAlgorithmException;

    String addAddress(@Valid AddUserAddressDto addUserAddressDto);

    UserDto getUserByUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);

    UserSessionDto login(@Valid LoginDto loginDto) throws AuthException, NoSuchAlgorithmException;
}
