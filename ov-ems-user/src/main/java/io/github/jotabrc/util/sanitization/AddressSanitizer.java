package io.github.jotabrc.util.sanitization;

import io.github.jotabrc.model.Address;
import io.github.jotabrc.ov_sanitizer.Sanitizer;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressSanitizer implements SanitizerInterface<Address, Address> {

    private final CitySanitizer citySanitizer;
    private final Sanitizer<String, String> sanitizer;

    @Autowired
    public AddressSanitizer(CitySanitizer citySanitizer, Sanitizer<String, String> sanitizer) {
        this.citySanitizer = citySanitizer;
        this.sanitizer = sanitizer;
    }

    @Override
    public Address sanitize(@NotNull Address address) {
        address
                .setPostalCode(sanitizer.sanitize(address.getPostalCode()))
                .setStreet(sanitizer.sanitize(address.getStreet()))
                .setNumber(sanitizer.sanitize(address.getNumber()))
                .setCity(citySanitizer.sanitize(address.getCity()));

        return address;
    }
}
