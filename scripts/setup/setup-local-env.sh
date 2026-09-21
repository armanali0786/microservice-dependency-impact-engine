#!/usr/bin/env bash
set -euo pipefail

cp -n .env.example .env || true
docker compose up -d postgres kafka redis otel-collector
echo "Local infrastructure is starting. Run scripts/setup/run-migrations.sh once Postgres is ready."
