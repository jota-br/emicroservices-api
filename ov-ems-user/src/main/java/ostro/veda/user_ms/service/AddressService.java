package ostro.veda.user_ms.service;

import ostro.veda.user_ms.dto.AddressDto;

public interface AddressService {

    AddressDto getAddressByUuid(String uuid);
}
