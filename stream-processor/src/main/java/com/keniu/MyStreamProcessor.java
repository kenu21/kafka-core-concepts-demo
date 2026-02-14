package com.keniu;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MyStreamProcessor {

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> source = streamsBuilder.stream("test-topic");
        KStream<String, String> processed = source
                .mapValues(value -> "Processed -> " + value)
                .peek((key, value) -> System.out.println(value));
        processed.to("processed-topic");
        return processed;
    }
}
