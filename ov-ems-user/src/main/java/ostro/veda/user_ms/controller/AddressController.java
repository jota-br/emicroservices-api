package ostro.veda.user_ms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ostro.veda.user_ms.dto.AddressDto;
import ostro.veda.user_ms.response.ResponseBody;
import ostro.veda.user_ms.response.ResponsePayload;
import ostro.veda.user_ms.service.AddressService;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static ostro.veda.user_ms.controller.ControllerDefaults.MAPPING_PREFIX;
import static ostro.veda.user_ms.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/address")
@RestController
@Tag(name = "Address Controller", description = "Manage Address operations")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponsePayload<AddressDto>> getUserByUuid(@PathVariable("uuid") final String uuid) throws NoSuchAlgorithmException {
        AddressDto addressDto = addressService.getAddressByUuid(uuid);
        return ResponseEntity.ok(new ResponsePayload<AddressDto>()
                .setMessage("Address with uuid %s found".formatted(uuid))
                .setBody(new ResponseBody<AddressDto>()
                        .setData(List.of(addressDto))));
    }
}
