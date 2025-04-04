package ostro.veda.user_ms.service;

import ostro.veda.user_ms.dto.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface UserService {

    String add(AddUserDto addUserDto) throws NoSuchAlgorithmException;

    void update(UpdateUserDto updateUserDto);

    void updatePassword(UpdateUserPasswordDto updateUserPasswordDto) throws NoSuchAlgorithmException;

    String addAddress(AddUserAddressDto addUserAddressDto);

    UserDto getUserByUuid(String uuid);

    UserSessionDto login(LoginDto loginDto) throws InvalidKeyException, NoSuchAlgorithmException;
}
