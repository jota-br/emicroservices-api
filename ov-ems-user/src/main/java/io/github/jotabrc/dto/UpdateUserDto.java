package io.github.jotabrc.dto;

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
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phone;
}
