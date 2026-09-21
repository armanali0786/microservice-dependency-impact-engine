#!/usr/bin/env bash
set -euo pipefail

mvn -B verify -Pintegration-test
