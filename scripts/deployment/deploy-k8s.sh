#!/usr/bin/env bash
set -euo pipefail

kubectl apply -f infrastructure/kubernetes/namespace
kubectl apply -f infrastructure/kubernetes/config
kubectl apply -f infrastructure/kubernetes/secrets
kubectl apply -f infrastructure/kubernetes/gateway
kubectl apply -f infrastructure/kubernetes/deployments
kubectl apply -f infrastructure/kubernetes/services
kubectl apply -f infrastructure/kubernetes/ingress
kubectl apply -f infrastructure/kubernetes/autoscaling
