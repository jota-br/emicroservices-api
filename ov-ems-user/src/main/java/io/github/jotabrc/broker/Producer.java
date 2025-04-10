package io.github.jotabrc.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class Producer {

    private static final Properties properties = getProperties();

    private static Properties getProperties() {
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        props.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");

        return props;
    }

    public <T> void producer(T t) throws JsonProcessingException {
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(t);
        ProducerRecord<String, String> record = new ProducerRecord<>("topic", "key", json);
        producer.send(record);
        producer.close();
    }
}
