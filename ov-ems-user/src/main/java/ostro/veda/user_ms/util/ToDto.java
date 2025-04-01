package ostro.veda.user_ms.util;

import ostro.veda.user_ms.dto.*;
import ostro.veda.user_ms.model.Address;
import ostro.veda.user_ms.model.Role;
import ostro.veda.user_ms.model.User;

public class ToDto {

    public static UserDto toDto(User user) {
        return UserDto
                .builder()
                .uuid(user.getUuid())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(toDto(user.getRole()))
                .address(user.getAddress()
                        .stream()
                        .map(ToDto::toDto)
                        .toList())
                .build();
    }

    public static RoleDto toDto(Role role) {
        return RoleDto
                .builder()
                .uuid(role.getUuid())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    public static AddressDto toDto(Address address) {
        return AddressDto
                .builder()
                .uuid(address.getUuid())
                .postalCode(address.getPostalCode())
                .street(address.getStreet())
                .number(address.getNumber())
                .type(address.getType())
                .city(
                        CityDto
                                .builder()
                                .uuid(address.getCity().getUuid())
                                .name(address.getCity().getName())
                                .state(
                                        StateDto
                                                .builder()
                                                .uuid(address.getCity().getState().getUuid())
                                                .name(address.getCity().getState().getName())
                                                .country(
                                                        CountryDto
                                                                .builder()
                                                                .uuid(address.getCity().getState().getCountry().getUuid())
                                                                .name(address.getCity().getState().getCountry().getName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}
