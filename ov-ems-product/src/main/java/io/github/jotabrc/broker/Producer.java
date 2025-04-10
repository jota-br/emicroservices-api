package io.github.jotabrc.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Properties;

@Component
public class Producer {

    @Autowired
    private BrokerConfig brokerConfig;

    private final Properties properties = getProperties();

    private Properties getProperties() {
        Properties props = new Properties();

        Optional<String> servers = Optional.ofNullable(brokerConfig.getBootstrapServers());
        Optional<String> key = Optional.ofNullable(brokerConfig.getKeyDeserializer());
        Optional<String> value = Optional.ofNullable(brokerConfig.getValueDeserializer());

        props.put("bootstrap.servers", servers.orElse("localhost:9092"));
        props.put("key.serializer", key.orElse("org.springframework.kafka.support.serializer.JsonSerializer"));
        props.put("value.serializer", value.orElse("org.springframework.kafka.support.serializer.JsonSerializer"));

        return props;
    }

    public <T> void producer(T t) throws JsonProcessingException {
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        Optional<String> topic = Optional.ofNullable(brokerConfig.getValueDeserializer());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(t);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic.orElse("inventory-add-item"), "key", json);
        producer.send(record);
        producer.close();
    }
}
