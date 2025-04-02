package io.github.jotabrc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class UserDto {

    private final String uuid;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final boolean isActive;
    private final RoleDto role;
    private final List<AddressDto> address;
}
