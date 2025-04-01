package ostro.veda.user_ms.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ostro.veda.user_ms.dto.*;
import ostro.veda.user_ms.model.*;
import ostro.veda.user_ms.repository.*;
import ostro.veda.user_ms.security.JWTCreator;
import ostro.veda.user_ms.security.JWTObject;
import ostro.veda.user_ms.security.SecurityConfig;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static ostro.veda.user_ms.util.ToDto.toDto;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CityRepository cityRepository, StateRepository stateRepository, CountryRepository countryRepository) {
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

        User user = build(addUserDto);

        return userRepository.save(user).getUuid();
    }

    @Override
    public void update(final UpdateUserDto updateUserDto) {
        User user = userRepository.findByUuid(updateUserDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("User with uuid %s not found".formatted(updateUserDto.getUuid())));

        updateUser(user, updateUserDto);

        userRepository.save(user);
    }

    @Override
    public void updatePassword(final UpdateUserPasswordDto updateUserPasswordDto) throws NoSuchAlgorithmException {
        User user = userRepository.findByUuid(updateUserPasswordDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("User with uuid %s not found".formatted(updateUserPasswordDto.getUuid())));

        updatePassword(user, updateUserPasswordDto);

        userRepository.save(user);
    }

    @Override
    public String addAddress(AddUserAddressDto addUserAddressDto) {
        User user = userRepository.findByUuid(addUserAddressDto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("User with uuid %s not found".formatted(addUserAddressDto.getUuid())));

        user.getAddress().forEach(address -> address.setActive(false));
        addAddress(user, addUserAddressDto);
        return userRepository.save(user).getAddress().get(user.getAddress().size() - 1).getUuid();
    }

    @Override
    public UserDto getUserByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User with uuid %s not found".formatted(uuid)));

        return toDto(user);
    }

    @Override
    public UserSessionDto login(LoginDto loginDto) throws InvalidKeyException, NoSuchAlgorithmException {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username %s not found"
                        .formatted(loginDto.getUsername())));

        if (!getHash(loginDto.getPassword(), user.getSalt()).equals(user.getHash()))
            throw new InvalidKeyException("Password doesn't match");

        JWTObject jwtObject = new JWTObject();
        jwtObject.setSubject(user.getUsername());
        jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
        jwtObject.setExpiration((new Date(System.currentTimeMillis() + SecurityConfig.EXPIRATION)));
        jwtObject.setRoles(List.of(user.getRole().getName()));


        return UserSessionDto
                .builder()
                .user(user.getUsername())
                .token(JWTCreator.create(SecurityConfig.PREFIX, SecurityConfig.KEY, jwtObject))
                .build();
    }

    private User build(final AddUserDto addUserDto) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(addUserDto.getPassword(), salt);

        Role role = roleRepository.findByName("GUESTS")
                .orElseThrow(() -> new EntityNotFoundException("Role with name GUESTS not found"));

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
