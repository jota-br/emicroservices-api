package io.github.jotabrc.broker;

import io.github.jotabrc.ov_kafka_cp.TopicConstant;
import io.github.jotabrc.ov_kafka_cp.broker.Producer;
import io.github.jotabrc.ovauth.header.Header;
import io.github.jotabrc.ovauth.token.SecurityHeader;
import io.github.jotabrc.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class ConsumerListener {

    private final OrderService orderService;
    private final Producer producer;

    @Autowired
    public ConsumerListener(OrderService orderService, Producer producer) {
        this.orderService = orderService;
        this.producer = producer;
    }

    @KafkaListener(topics = TopicConstant.ORDER_CANCEL,
            groupId = "io.github.jotabrc", containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> record) {
        String token = new String(record.headers().lastHeader(Header.X_SECURE_ORIGIN.getHeader()).value());
        String data = new String(record.headers().lastHeader(Header.X_SECURE_DATA.getHeader()).value());

        try {
            SecurityHeader.compare(data, token);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

//        ObjectMapper objectMapper = new ObjectMapper();

        switch (record.topic()) {
            case TopicConstant.ORDER_CANCEL -> {
                String orderUuid = record.value();
                orderService.cancelOrder(orderUuid);
            }
        }
    }
}
