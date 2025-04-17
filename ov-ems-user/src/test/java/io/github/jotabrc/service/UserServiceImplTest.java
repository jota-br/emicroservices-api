package io.github.jotabrc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.security.NoSuchAlgorithmException;

@SpringBootTest
@ActiveProfiles("dev")
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void add() throws NoSuchAlgorithmException {
//        AddUserDto invalid = AddUserDto
//                .builder()
//                .username("username&32")
//                .email("user@email.com")
//                .password("password@&edJh64") // invalid
//                .firstName("First")
//                .lastName("Last")
//                .phone("+5511123456789")
//                .build();
//
//        assertThrows(ConstraintViolationException.class, () -> userService.add(invalid));
//
//        AddUserDto addUserDto = AddUserDto
//                .builder()
//                .username("username32")
//                .email("user@email.com")
//                .password("passwordedJh64")
//                .firstName("First")
//                .lastName("Last")
//                .phone("+5511123456789")
//                .build();
//        assertNotNull(userService.add(addUserDto));
    }

    @Test
    void updatePassword() {
    }

    @Test
    void addAddress() {
    }

    @Test
    void update() throws NoSuchAlgorithmException {
    }

    @Test
    void getUserByUuid() {
    }

    @Test
    void login() {
    }
}