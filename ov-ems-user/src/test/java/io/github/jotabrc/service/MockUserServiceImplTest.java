package io.github.jotabrc.service;

import io.github.jotabrc.dto.*;
import io.github.jotabrc.model.*;
import io.github.jotabrc.ov_auth_validator.authorization.UsernameAuthorizationValidator;
import io.github.jotabrc.ov_auth_validator.util.UserRoles;
import io.github.jotabrc.ovauth.jwt.TokenCreator;
import io.github.jotabrc.ovauth.jwt.TokenObject;
import io.github.jotabrc.repository.*;
import io.github.jotabrc.util.sanitization.UserSanitizer;
import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
class MockUserServiceImplTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private TokenCreator tokenCreator;

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
    void add() throws NoSuchAlgorithmException {
        AddUserDto addUserDto = AddUserDto
                .builder()
                .username("username")
                .password("password")
                .email("email")
                .firstName("name")
                .lastName("last")
                .phone("phone")
                .build();

        String uuid = UUID.randomUUID().toString();
        when(userRepository.save(any())).thenReturn(new User().setUuid(uuid));
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByPhone(any())).thenReturn(false);
        when(roleRepository.findByName(any())).thenReturn(Optional.ofNullable(Role.builder().build()));
        String resultUuid = userService.add(addUserDto);
        assertEquals(uuid, resultUuid);
    }

    @Test
    void updatePassword() throws NoSuchAlgorithmException {
        doNothing().when(usernameAuthorizationValidator).validate(any());
        User user = new User()
                .setHash("hash")
                .setSalt("salt")
                .setUuid("uuid");
        when(userRepository.findByUuid("uuid")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(null);

        UpdateUserPasswordDto updateUserPasswordDto = UpdateUserPasswordDto
                .builder()
                .password("value")
                .uuid("uuid")
                .build();
        userService.updatePassword(updateUserPasswordDto);

        assertNotEquals("hash", user.getHash());
        assertNotEquals("salt", user.getSalt());
    }

    @Test
    void addAddress() {
        doNothing().when(usernameAuthorizationValidator).validate(any());
        User user = new User()
                .setHash("hash")
                .setSalt("salt")
                .setUuid("uuid")
                .setAddress(new ArrayList<>());
        when(userRepository.findByUuid("uuid")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        AddUserAddressDto addUserAddressDto = AddUserAddressDto
                .builder()
                .uuid("uuid")
                .address(
                        AddressDto
                                .builder()
                                .postalCode("11444000")
                                .street("St. Example")
                                .number("B-304")
                                .type(AddressType.HOME)
                                .city(
                                        CityDto
                                                .builder()
                                                .name("City of Example")
                                                .state(
                                                        StateDto
                                                                .builder()
                                                                .name("State of a Country")
                                                                .country(
                                                                        CountryDto
                                                                                .builder()
                                                                                .name("United Country of a Nation")
                                                                                .build()
                                                                )
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        when(countryRepository.findByName(any())).thenReturn(Optional.of(new Country()));
        when(stateRepository.findByName(any())).thenReturn(Optional.of(new State()));
        when(cityRepository.findByName(any())).thenReturn(Optional.of(new City()));

        String uuid = userService.addAddress(addUserAddressDto);
        assertEquals(1, user.getAddress().size());
        assertNotNull(uuid);
    }

    @Test
    void getUserByUuid() {
        doNothing().when(usernameAuthorizationValidator).validate(any());
        User user = new User()
                .setUuid("uuid")
                .setRole(new Role())
                .setAddress(new ArrayList<>());
        when(userRepository.findByUuid("uuid")).thenReturn(Optional.of(user));
        var result = userService.getUserByUuid("uuid");
        assert result.getUuid().equals("uuid");
    }

    @Test
    void login() throws AuthException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException {
        User user = new User()
                .setUuid("uuid")
                .setUsername("username")
                .setHash("h50rZTkC3PWiu4hjyvowdht3BzuR4EkTW4dbfYSABVbJ7vLVZwlO8Mihj4HqLTOxoo15aWkOPRVCpASig0qRcw==")
                .setSalt("salt")
                .setRole(new Role().setName(UserRoles.ADMIN.getName()));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        LoginDto loginDto = LoginDto
                .builder()
                .username("username")
                .password("password")
                .build();

        try (MockedStatic<TokenCreator> mockedStatic = mockStatic(TokenCreator.class)) {
            mockedStatic.when(() -> TokenCreator.create(eq("Bearer"), eq("key"), any(TokenObject.class)))
                    .thenReturn("token-mock");
            var result = userService.login(loginDto);
            assert result.getUser().equals(loginDto.getUsername());
        }
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