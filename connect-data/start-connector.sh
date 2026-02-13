#!/bin/bash
set -e

CONFIG_FILE="$(dirname "$0")/file-source.json"

CONNECT_URL="http://localhost:8083/connectors"

echo "Creating connector..."
curl -X POST -H "Content-Type: application/json" --data @"$CONFIG_FILE" "$CONNECT_URL"

echo -e "\nDone!"
