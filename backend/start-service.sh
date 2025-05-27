#!/bin/bash

# Check if JAVA_OPTS or JVM_ARGS are empty and initialize them if needed
JAVA_OPTS="${JAVA_OPTS:-}"
JVM_ARGS="${JVM_ARGS:-}"

# Define the startup command with safer handling
STARTUP_COMMAND="java $JVM_ARGS $JAVA_OPTS \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:OnError='echo JVM exited with error' \
  -XX:MaxRAMPercentage=75.0 \
  -XX:MinRAMPercentage=75.0 \
  -jar /etc/shavika-websocket/shavika-websocket-standalone-1.0.0.jar"

# Log the command to ensure it's constructed correctly (optional for debugging)
echo "Starting application with command: $STARTUP_COMMAND"

# Execute the command and capture its output for debugging
eval $STARTUP_COMMAND
EXIT_CODE=$?

# Handle errors and log
if [ $EXIT_CODE -ne 0 ]; then
  echo "Application failed to start with exit code $EXIT_CODE" >&2
  exit $EXIT_CODE
fi
