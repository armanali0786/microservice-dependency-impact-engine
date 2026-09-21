package com.dependencyimpact.runtimeanalysis.config;

// Deferred alongside OtelExportConsumer - no real OTel SDK/Collector wiring is
// needed while ingestion goes through the REST endpoint instead.
@org.springframework.context.annotation.Configuration
public class OpenTelemetryConfig {
}
