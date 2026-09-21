#!/usr/bin/env bash
set -euo pipefail

SERVICES=(api-gateway service-registry dependency-collector dependency-graph-service impact-analysis-service runtime-analysis-service notification-service)

for service in "${SERVICES[@]}"; do
  docker build -f "backend/${service}/Dockerfile" -t "microservice-impact/${service}:latest" .
done
