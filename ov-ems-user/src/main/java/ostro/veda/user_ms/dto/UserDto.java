package ostro.veda.user_ms.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ostro.veda.user_ms.model.Address;
import ostro.veda.user_ms.model.Role;

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
    private final Role role;
    private final List<Address> address;
}
