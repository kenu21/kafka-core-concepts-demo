#!/bin/bash

/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic orders \
  --partitions 3 \
  --replication-factor 1

/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic orders-compacted \
  --partitions 3 \
  --config cleanup.policy=compact
