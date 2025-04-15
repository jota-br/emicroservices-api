package io.github.jotabrc.service;

import io.github.jotabrc.dto.UpdateUserDto;
import io.github.jotabrc.model.User;
import io.github.jotabrc.ov_auth_validator.authorization.UsernameAuthorizationValidator;
import io.github.jotabrc.repository.UserRepository;
import io.github.jotabrc.util.sanitization.UserSanitizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
class MockUserServiceImplTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsernameAuthorizationValidator usernameAuthorizationValidator;

    @Mock
    private UserSanitizer userSanitizer;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void update() throws NoSuchAlgorithmException {
        String uuid = UUID.randomUUID().toString();

        UpdateUserDto updateUserDto = UpdateUserDto
                .builder()
                .uuid(uuid)
                .username("newUsername")
                .email("newUsername@example.com")
                .firstName("John")
                .lastName("Doe")
                .phone("+5599987654321")
                .build();

        User user = User
                .builder()
                .uuid(uuid)
                .username("newUsername")
                .email("newUsername@example.com")
                .firstName("John")
                .lastName("Doe")
                .phone("+5599987654321")
                .build();


        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        userService.update(updateUserDto);

        verify(usernameAuthorizationValidator).validate(user.getUsername());
        verify(userRepository).save(user);
        verify(userSanitizer).sanitize(user);

        assertEquals("newUsername", user.getUsername());
        assertEquals("newUsername@example.com", user.getEmail());
    }
}