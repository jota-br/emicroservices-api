package io.github.jotabrc.broker;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Component
public class Consumer implements CommandLineRunner {

    private Properties properties;

    private Properties getProperties() {
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "io.github.jotabrc");
        props.put("key.deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer");
        props.put("value.deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer");

        return props;
    }

    public void consumer() {
        KafkaConsumer<String, String> consumer = null;
        try {
            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Collections.singletonList("topic"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                records.forEach(record -> System.out.println(record.value()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (consumer != null)
                consumer.close();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        properties = getProperties();
        consumer();
    }
}
