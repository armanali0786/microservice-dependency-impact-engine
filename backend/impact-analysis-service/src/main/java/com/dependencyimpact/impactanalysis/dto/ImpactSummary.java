package com.dependencyimpact.impactanalysis.dto;

// Deliberately smaller than the "affectedTeams" summary shown in docs/api-structure.md #65 -
// computing that would need a cross-service lookup into service-registry's team ownership
// that isn't wired up yet. Flagging this as a known gap rather than faking the number.
public class ImpactSummary {

    private final int directConsumers;
    private final int indirectConsumers;
    private final int affectedKafkaTopics;

    public ImpactSummary(int directConsumers, int indirectConsumers, int affectedKafkaTopics) {
        this.directConsumers = directConsumers;
        this.indirectConsumers = indirectConsumers;
        this.affectedKafkaTopics = affectedKafkaTopics;
    }

    public int getDirectConsumers() {
        return directConsumers;
    }

    public int getIndirectConsumers() {
        return indirectConsumers;
    }

    public int getAffectedKafkaTopics() {
        return affectedKafkaTopics;
    }
}
