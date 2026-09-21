package com.dependencyimpact.impactanalysis.client;

import org.springframework.stereotype.Component;

import java.util.UUID;

// runtime-analysis-service doesn't exist yet (that's a later milestone), so there's
// nothing to call. This always reports "no evidence" rather than guessing, and the
// call site (RiskClassificationService) treats that as a neutral signal, not a
// negative one - so risk classification today is based purely on the declared
// dependency graph (criticality/failure-behavior), same as the docs' algorithm
// before runtime evidence is layered in.
@Component
public class RuntimeAnalysisClient {

    public boolean hasRuntimeEvidence(UUID sourceServiceId, UUID targetServiceId) {
        return false;
    }
}
