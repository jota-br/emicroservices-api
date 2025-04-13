package io.github.jotabrc.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jotabrc.dto.AddItemDto;
import io.github.jotabrc.dto.AddOrderDto;
import io.github.jotabrc.dto.UpdateProductNameDto;
import io.github.jotabrc.dto.UpdateProductStockDto;
import io.github.jotabrc.ov_kafka_cp.TopicConstant;
import io.github.jotabrc.ov_kafka_cp.broker.Producer;
import io.github.jotabrc.ovauth.header.Header;
import io.github.jotabrc.ovauth.token.SecurityHeader;
import io.github.jotabrc.service.ItemService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class ConsumerListener {

    private final ItemService itemService;
    private final Producer producer;

    @Autowired
    public ConsumerListener(ItemService itemService, Producer producer) {
        this.itemService = itemService;
        this.producer = producer;
    }

    @KafkaListener(topics = TopicConstant.INVENTORY_ADD_ITEM + "," +
            TopicConstant.INVENTORY_UPDATE_NAME + "," +
            TopicConstant.INVENTORY_ADD_ORDER + "," +
            TopicConstant.INVENTORY_CANCEL_ORDER + "," +
            TopicConstant.INVENTORY_UPDATE_STOCK,
            groupId = "io.github.jotabrc", containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> record) {
        String token = new String(record.headers().lastHeader(Header.X_SECURE_ORIGIN.getHeader()).value());
        String data = new String(record.headers().lastHeader(Header.X_SECURE_DATA.getHeader()).value());

        try {
            SecurityHeader.compare(data, token);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            switch (record.topic()) {
                case TopicConstant.INVENTORY_ADD_ITEM -> {
                    AddItemDto addItemDto = objectMapper.readValue(record.value(), AddItemDto.class);
                    itemService.add(addItemDto);
                }
                case TopicConstant.INVENTORY_UPDATE_NAME -> {
                    UpdateProductNameDto updateProductNameDto = objectMapper.readValue(record.value(), UpdateProductNameDto.class);
                    itemService.updateName(updateProductNameDto);
                }
                case TopicConstant.INVENTORY_ADD_ORDER, TopicConstant.INVENTORY_CANCEL_RESERVED_ORDER -> {
                    AddOrderDto addOrderDto = objectMapper.readValue(record.value(), AddOrderDto.class);
                    itemService.updateReserve(addOrderDto);
                }
                case TopicConstant.INVENTORY_CANCEL_ORDER -> {
                    UpdateProductStockDto updateProductStockDto = objectMapper.readValue(record.value(), UpdateProductStockDto.class);
                    itemService.updateStock(updateProductStockDto);
                }
            }
        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
