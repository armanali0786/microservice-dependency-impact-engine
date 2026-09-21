#!/usr/bin/env bash
set -euo pipefail

flyway -url="${DB_URL:-jdbc:postgresql://localhost:5432/dependency_impact}" \
       -user="${DB_USERNAME:-app_user}" \
       -password="${DB_PASSWORD:-changeme}" \
       -locations="filesystem:database/migrations" \
       migrate
