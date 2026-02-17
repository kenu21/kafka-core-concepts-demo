#!/bin/bash

echo "Waiting for Kafka to be ready..." 1>&2

for i in {1..60}; do
  if /opt/kafka/bin/kafka-broker-api-versions.sh --bootstrap-server kafka:9092 >/dev/null 2>&1; then
    echo "Kafka is ready!" 1>&2
    break
  fi
  echo "Still waiting... ($i/60)" 1>&2
  sleep 2
done

echo "Creating ACL for transactional IDs (ALL)..." 1>&2
/opt/kafka/bin/kafka-acls.sh --bootstrap-server kafka:9092 \
  --add \
  --allow-principal User:admin \
  --operation ALL \
  --transactional-id '*' 2>&1

echo "Creating ACL for topics (Write)..." 1>&2
/opt/kafka/bin/kafka-acls.sh --bootstrap-server kafka:9092 \
  --add \
  --allow-principal User:admin \
  --operation Write \
  --topic '*' 2>&1

echo "=== ACL initialization completed ===" 1>&2
