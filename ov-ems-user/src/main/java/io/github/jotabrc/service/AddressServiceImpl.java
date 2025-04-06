package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddressDto;
import io.github.jotabrc.model.Address;
import io.github.jotabrc.ov_auth_validator.authorization.UsernameAuthorizationValidator;
import io.github.jotabrc.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.github.jotabrc.util.ToDto.toDto;

@Service
public class AddressServiceImpl implements AddressService {

    private final UsernameAuthorizationValidator usernameAuthorizationValidator;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(UsernameAuthorizationValidator usernameAuthorizationValidator, AddressRepository addressRepository) {
        this.usernameAuthorizationValidator = usernameAuthorizationValidator;
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDto getAddressByUuid(final String uuid) {
        Address address = addressRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Address with uuid %s not found".formatted(uuid)));

        usernameAuthorizationValidator.validate(address.getUser().getUsername());

        return toDto(address);
    }
}
