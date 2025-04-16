package io.github.jotabrc.controller;

import io.github.jotabrc.dto.*;
import io.github.jotabrc.model.AddressType;
import io.github.jotabrc.ovauth.jwt.TokenConfig;
import io.github.jotabrc.ovauth.jwt.TokenObject;
import io.github.jotabrc.service.ActivationTokenServiceImpl;
import io.github.jotabrc.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    ActivationTokenServiceImpl activationTokenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    void add() throws Exception {
        String uuid = UUID.randomUUID().toString();
        when(userService.add(any())).thenReturn(uuid);
        doNothing().when(activationTokenService).add(any());

        String addUser = """
                {
                  "uuid": "string",
                  "username": "string",
                  "email": "example@example.com",
                  "password": "password90@&",
                  "firstName": "Johnson",
                  "lastName": "Johnson",
                  "phone": "+5547111111111"
                }
                """;

        mockMvc.perform(post("/api/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addUser))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("User inserted with uuid %s".formatted(uuid));
                });
    }

    @Test
    void addAddress() throws Exception {
        String uuid = UUID.randomUUID().toString();
        AddUserAddressDto addUserAddressDto = AddUserAddressDto
                .builder()
                .uuid(uuid)
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

        when(userService.addAddress(any())).thenReturn(addUserAddressDto.getUuid());

        String json = """
                {
                  "uuid": "%s",
                  "address": {
                    "postalCode": "string",
                    "street": "string",
                    "number": "string",
                    "type": "HOME",
                    "city": {
                      "name": "string",
                      "state": {
                        "name": "string",
                        "country": {
                          "name": "string"
                        }
                      }
                    }
                  }
                }
                """.formatted(uuid);

        mockMvc.perform(post("/api/v1/user/add/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("User with uuid %s Address inserted".formatted(uuid));

                });
    }

    @Test
    void update() throws Exception {
        String uuid = UUID.randomUUID().toString();
        doNothing().when(userService).update(any());

        String json = """
                {
                  "uuid": "%s",
                  "username": "string",
                  "email": "string",
                  "firstName": "string",
                  "lastName": "string",
                  "phone": "string"
                }
                """.formatted(uuid);

        mockMvc.perform(put("/api/v1/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("User with uuid %s has been updated".formatted(uuid));

                });
    }

    @Test
    void updatePassword() throws Exception {
        doNothing().when(userService).updatePassword(any());
        String uuid = UUID.randomUUID().toString();
        String json = """
                {
                    "uuid": "%s",
                    "password": "password1234"
                }
                """.formatted(uuid);

        mockMvc.perform(put("/api/v1/user/update/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("User with uuid %s password updated".formatted(uuid));

                });
    }

    @Test
    void getUserByUuid() throws Exception {
        String uuid = UUID.randomUUID().toString();
        UserDto userDto = UserDto
                .builder()
                .uuid(uuid)
                .username("username32")
                .email("user@email.com")
                .firstName("First")
                .lastName("Last")
                .phone("+5511123456789")
                .build();
        when(userService.getUserByUuid(userDto.getUuid())).thenReturn(userDto);
        UserDto result = userService.getUserByUuid(uuid);
        assertEquals(userDto.getUuid(), result.getUuid());
        assertEquals(userDto.getUsername(), result.getUsername());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getPhone(), result.getPhone());

        mockMvc.perform(get("/api/v1/user/get/uuid/" + userDto.getUuid()))
                .andExpect(status().isOk());
    }

    @Test
    void login() throws Exception {

        TokenObject tokenObject = TokenObject
                .builder()
                .subject("username")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TokenConfig.EXPIRATION))
                .roles(List.of("ADMIN"))
                .build();
        String jwt = "jwt-mocked-token";

        when(userService.login(any()))
                .thenReturn(
                        UserSessionDto
                                .builder()
                                .user(tokenObject.getSubject())
                                .token(jwt)
                                .build()
                );

        String userCredentials = """
                {
                    "username": "username",
                    "password": "password"
                }
                """;
        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userCredentials))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("jwt-mocked-token");
                });

        verify(userService).login(any(LoginDto.class));
    }
}