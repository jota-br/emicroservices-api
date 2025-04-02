package io.github.jotabrc.service;

import io.github.jotabrc.dto.AddressDto;

public interface AddressService {

    AddressDto getAddressByUuid(String uuid);
}
