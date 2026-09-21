package com.dependencyimpact.runtimeanalysis.scheduler;

// Deferred: retention/cleanup policy for runtime_observations is its own concern
// (see docs/DATA_RETENTION_AND_LIFECYCLE.md) with its own tradeoffs - how long to
// keep raw observations vs. rolled-up aggregates, per-environment policy, etc.
// Bolting a guessed "delete anything older than N days" onto this milestone would
// just be a policy decision made silently. Left unimplemented on purpose.
@org.springframework.stereotype.Component
public class OldObservationCleanupJob {
}
