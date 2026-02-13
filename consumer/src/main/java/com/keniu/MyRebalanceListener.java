package com.keniu;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.jspecify.annotations.NonNull;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyRebalanceListener implements ConsumerAwareRebalanceListener {

    private final Map<TopicPartition, Long> offsets = new HashMap<>();
    private Consumer<?, ?> consumer;

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        partitions.forEach(topicPartition -> {
            Long offset = offsets.get(topicPartition);
            if (offset != null) {
                consumer.commitSync(Map.of(topicPartition, new OffsetAndMetadata(offset)));
                System.out.println("Committed offset " + offset + " for " + topicPartition);
            }
        });
    }

    @Override
    public void onPartitionsAssigned(@NonNull Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        this.consumer = consumer;

        partitions.forEach(topicPartition -> {
            consumer.seek(topicPartition, 0);
            offsets.put(topicPartition, 0L);
            System.out.println("Assigned " + topicPartition + ", starting from offset 0");
        });
    }

    public void updateOffset(TopicPartition topicPartition, Long offset) {
        offsets.put(topicPartition, offset);
    }
}
