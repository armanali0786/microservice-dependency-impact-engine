package com.dependencyimpact.runtimeanalysis.kafka.consumer;

// Deferred: the real ingestion path (docs/technical-implementation.md section 29 -
// Application -> OpenTelemetry SDK -> Collector -> Kafka -> here) needs an actual
// OpenTelemetry Collector exporting traces, which nothing in this learning setup
// runs. POST /api/v1/runtime/observations (TelemetryIngestionService) is the
// practical MVP ingestion path instead - same reasoning as the five
// DependencyCollector stubs deferred in Milestone 2.
@org.springframework.stereotype.Component
public class OtelExportConsumer {
}
