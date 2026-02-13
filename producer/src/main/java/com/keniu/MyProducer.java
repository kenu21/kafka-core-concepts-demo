package com.keniu;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

public class MyProducer {
    private static final long ONE_SECOND = 1000;
    private static final int BATCH_SIZE_32_KB = 32_768;
    private static final int LINGER_TIME_250_MS = 250;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public MyProducer() {
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory());
    }

    public void startProducing() {
        int counter = 0;
        while (true) {
            String message = "Message #" + counter++;
            kafkaTemplate.executeInTransaction(kafkaOperations -> {
                kafkaOperations.send("test-topic", "my-key", message);
                System.out.println("Sent: " + message);
                return true;
            });
            try {
                Thread.sleep(ONE_SECOND);
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9097");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        configs.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configs.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
        configs.put(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE_32_KB);
        configs.put(ProducerConfig.LINGER_MS_CONFIG, LINGER_TIME_250_MS);
        return new DefaultKafkaProducerFactory<>(configs);
    }
}
