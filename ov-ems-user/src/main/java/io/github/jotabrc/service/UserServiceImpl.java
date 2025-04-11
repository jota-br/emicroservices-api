package io.github.jotabrc.service;

import io.github.jotabrc.dto.*;
import io.github.jotabrc.model.*;
import io.github.jotabrc.ov_auth_validator.authorization.UsernameAuthorizationValidator;
import io.github.jotabrc.ov_auth_validator.util.UserRoles;
import io.github.jotabrc.ovauth.jwt.TokenConfig;
import io.github.jotabrc.ovauth.jwt.TokenCreator;
import io.github.jotabrc.ovauth.jwt.TokenObject;
import io.github.jotabrc.repository.*;
import io.github.jotabrc.util.sanitization.UserSanitizer;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static io.github.jotabrc.util.ToDto.toDto;

@Service
public class UserServiceImpl implements UserService {

    private final UserSanitizer userSanitizer;
    private final UsernameAuthorizationValidator usernameAuthorizationValidator;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public UserServiceImpl(UserSanitizer userSanitizer, UsernameAuthorizationValidator usernameAuthorizationValidator, UserRepository userRepository, RoleRepository roleRepository, CityRepository cityRepository,
                           StateRepository stateRepository, CountryRepository countryRepository) {
        this.userSanitizer = userSanitizer;
        this.usernameAuthorizationValidator = usernameAuthorizationValidator;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public String add(final AddUserDto addUserDto) throws NoSuchAlgorithmException {
        boolean exists = userRepository.existsByUsername(addUserDto.getUsername());
        if (exists) throw new EntityExistsException("Username %s already in use".formatted(addUserDto.getUsername()));

        exists = userRepository.existsByEmail(addUserDto.getEmail());
        if (exists) throw new EntityExistsException("Email %s already in use".formatted(addUserDto.getEmail()));

        exists = userRepository.existsByPhone(addUserDto.getPhone());
        if (exists) throw new EntityExistsException("Phone %s already in use".formatted(addUserDto.getPhone()));

        User user = build(addUserDto);
        sanitize(user);
        return userRepository.save(user).getUuid();
    }

    @Override
    public void update(final UpdateUserDto updateUserDto) {
        User user = userRepository.findByUuid(updateUserDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("User with uuid %s not found".formatted(updateUserDto.getUuid())));

        usernameAuthorizationValidator.validate(user.getUsername());

        updateUser(user, updateUserDto);
        sanitize(user);

        userRepository.save(user);
    }

    @Override
    public void updatePassword(final UpdateUserPasswordDto updateUserPasswordDto) throws NoSuchAlgorithmException {
        User user = userRepository.findByUuid(updateUserPasswordDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("User with uuid %s not found".formatted(updateUserPasswordDto.getUuid())));

        usernameAuthorizationValidator.validate(user.getUsername());

        updatePassword(user, updateUserPasswordDto);

        userRepository.save(user);
    }

    @Override
    public String addAddress(final AddUserAddressDto addUserAddressDto) {
        User user = userRepository.findByUuid(addUserAddressDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("User with uuid %s not found".formatted(addUserAddressDto.getUuid())));

        usernameAuthorizationValidator.validate(user.getUsername());

        user.getAddress().forEach(address -> address.setActive(false));
        addAddress(user, addUserAddressDto);
        sanitize(user);
        return userRepository.save(user).getAddress().get(user.getAddress().size() - 1).getUuid();
    }

    @Override
    public UserDto getUserByUuid(final String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User with uuid %s not found".formatted(uuid)));

        usernameAuthorizationValidator.validate(user.getUsername());

        return toDto(user);
    }

    @Override
    public UserSessionDto login(final LoginDto loginDto) throws AuthException, NoSuchAlgorithmException {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username %s not found"
                        .formatted(loginDto.getUsername())));

        if (!getHash(loginDto.getPassword(), user.getSalt()).equals(user.getHash()))
            throw new AuthException("Password doesn't match");

        TokenObject jwtObject = TokenObject
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TokenConfig.EXPIRATION))
                .roles(List.of(user.getRole().getName()))
                .build();

        return UserSessionDto
                .builder()
                .user(user.getUsername())
                .token(TokenCreator.create(TokenConfig.PREFIX, TokenConfig.KEY, jwtObject))
                .build();
    }

    private void sanitize(final User user) {
        userSanitizer.sanitize(user);
    }

    private User build(final AddUserDto addUserDto) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(addUserDto.getPassword(), salt);

        String defaultRole = UserRoles.GUEST.getName();
        Role role = roleRepository.findByName(defaultRole)
                .orElseThrow(() -> new EntityNotFoundException("Role with name %s not found".formatted(defaultRole)));

        return User
                .builder()
                .uuid(UUID.randomUUID().toString())
                .username(addUserDto.getUsername())
                .email(addUserDto.getEmail())
                .salt(encodedSalt)
                .hash(hash)
                .firstName(addUserDto.getFirstName())
                .lastName(addUserDto.getLastName())
                .phone(addUserDto.getPhone())
                .role(role)
                .isActive(false)
                .build();
    }

    private void updatePassword(final User user, final UpdateUserPasswordDto updateUserPasswordDto) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(updateUserPasswordDto.getPassword(), salt);

        user.setHash(hash);
        user.setSalt(encodedSalt);
    }

    private void updateUser(final User user, final UpdateUserDto updateUserDto) {
        user
                .setUsername(updateUserDto.getUsername())
                .setEmail(updateUserDto.getEmail())
                .setFirstName(updateUserDto.getFirstName())
                .setLastName(updateUserDto.getLastName())
                .setPhone(updateUserDto.getPhone());
    }

    private void addAddress(final User user, final AddUserAddressDto addUserAddressDto) {
        Country country = countryRepository.findByName(addUserAddressDto.getAddress().getCity().getName())
                .orElse(Country
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name(addUserAddressDto.getAddress().getCity().getState().getCountry().getName())
                        .isActive(true)
                        .build());
        State state = stateRepository.findByName(addUserAddressDto.getAddress().getCity().getName())
                .orElse(State
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name(addUserAddressDto.getAddress().getCity().getState().getName())
                        .country(country)
                        .build());
        City city = cityRepository.findByName(addUserAddressDto.getAddress().getCity().getName())
                .orElse(City
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name(addUserAddressDto.getAddress().getCity().getName())
                        .isActive(true)
                        .state(state)
                        .build());

        user
                .getAddress().add(
                        Address
                                .builder()
                                .user(user)
                                .uuid(UUID.randomUUID().toString())
                                .postalCode(addUserAddressDto.getAddress().getPostalCode())
                                .street(addUserAddressDto.getAddress().getStreet())
                                .number(addUserAddressDto.getAddress().getNumber())
                                .type(addUserAddressDto.getAddress().getType())
                                .isActive(true)
                                .city(city)
                                .build()
                );
    }

    private String getEncodedSalt(final byte[] salt) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(salt);
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);
        return salt;
    }

    private String getHash(final String password, final byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    private String getHash(final String password, final String salt) throws NoSuchAlgorithmException {
        byte[] saltByte = Base64.getDecoder().decode(salt);
        return getHash(password, saltByte);
    }
}