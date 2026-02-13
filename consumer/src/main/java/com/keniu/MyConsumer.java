package com.keniu;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MyConsumer {
    private final MyRebalanceListener myRebalanceListener;

    public MyConsumer(MyRebalanceListener myRebalanceListener) {
        this.myRebalanceListener = myRebalanceListener;
    }

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(
            ConsumerRecord<String, String> consumerRecord,
            Acknowledgment acknowledgment,
            Consumer<?, ?> consumer
    ) {
        System.out.println("Received: " + consumerRecord.value());

        TopicPartition topicPartition = new TopicPartition(consumerRecord.topic(), consumerRecord.partition());
        long endOffset = consumer.endOffsets(Collections.singleton(topicPartition)).get(topicPartition);
        long lag = endOffset - consumerRecord.offset() - 1;

        System.out.println("Partition: " + consumerRecord.partition()
                + ", Offset: " + consumerRecord.offset()
                + ", EndOffset: " + endOffset
                + ", Lag: " + lag);

        if (consumerRecord.value().equals("Message #1")) {
            throw new RuntimeException("Simulated processing failure");
        }

        acknowledgment.acknowledge();

        myRebalanceListener.updateOffset(topicPartition, consumerRecord.offset() + 1);
    }
}
