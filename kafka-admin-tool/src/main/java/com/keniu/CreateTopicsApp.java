package com.keniu;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CreateTopicsApp {
    private static final int PARTITIONS = 1;
    private static final short REPLICATION = 1;
    private static final String RETENTION_7_DAYS = "604800000";
    private static final String RETENTION_1_GB = "1073741824";

    static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9097");

        try (AdminClient adminClient = AdminClient.create(properties)) {

            adminClient.listTopics()
                    .names()
                    .get()
                    .forEach(System.out::println);

            NewTopic testTopic = new NewTopic("test-topic", PARTITIONS, REPLICATION);
            testTopic.configs(Map.of(
                    "retention.ms", RETENTION_7_DAYS,
                    "retention.bytes", RETENTION_1_GB
            ));

             NewTopic processedTopic = new NewTopic("processed-topic", PARTITIONS, REPLICATION);
            adminClient.createTopics(List.of(testTopic, processedTopic))
                    .all()
                    .get();
        }

        System.out.println("Kafka topics created successfully");
    }
}
