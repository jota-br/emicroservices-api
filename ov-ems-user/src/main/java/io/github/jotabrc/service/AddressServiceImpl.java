package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddressDto;
import io.github.jotabrc.model.Address;
import io.github.jotabrc.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ostro.veda.user_ms.util.AuthenticationHeader;

import static io.github.jotabrc.util.ToDto.toDto;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDto getAddressByUuid(String uuid) {
        Address address = addressRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Address with uuid %s not found".formatted(uuid)));

        AuthenticationHeader.check(address.getUser().getUsername());

        return toDto(address);
    }
}
