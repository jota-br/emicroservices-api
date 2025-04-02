package ostro.veda.user_ms.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ostro.veda.user_ms.dto.AddressDto;
import ostro.veda.user_ms.model.Address;
import ostro.veda.user_ms.repository.AddressRepository;
import ostro.veda.user_ms.util.AuthenticationHeader;

import static ostro.veda.user_ms.util.ToDto.toDto;

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
