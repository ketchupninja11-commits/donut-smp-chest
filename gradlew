#!/bin/sh
if ! command -v gradle >/dev/null 2>&1; then
  echo "Gradle is not installed. Install Gradle or generate a wrapper with: gradle wrapper --gradle-version 9.0"
  exit 1
fi
exec gradle "$@"
