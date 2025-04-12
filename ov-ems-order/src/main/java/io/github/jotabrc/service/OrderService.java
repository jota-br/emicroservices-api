package io.github.jotabrc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.dto.*;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Validated
public interface OrderService {

    String add(@Valid OrderCreationDto orderCreationDto);

    OrderDto getByOrderUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);

    List<OrderDto> getByUserUuid(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);

    void updateOrderStatus(@Valid OrderStatusUpdateDto orderStatusUpdateDto);

    void cancelOrder(@Valid @ValidateField(fieldName = "uuid", message = "Invalid UUID") String uuid);

    void returnItem(@Valid OrderReturnItemDto orderReturnItemDto) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;
}
