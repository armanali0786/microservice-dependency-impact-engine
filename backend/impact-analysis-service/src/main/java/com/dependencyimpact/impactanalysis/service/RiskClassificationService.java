package com.dependencyimpact.impactanalysis.service;

import com.dependencyimpact.common.model.DependencyCriticality;
import com.dependencyimpact.common.model.FailureBehavior;
import com.dependencyimpact.common.model.ImpactLevel;
import org.springframework.stereotype.Service;

// First-pass, deterministic ruleset - see docs/technical-implementation.md section 26
// for the shape ("Direct + FAIL_CLOSE + HIGH criticality + runtime confirmed").
// Runtime evidence is always false right now (RuntimeAnalysisClient has nothing to
// call yet), so today this only ever reasons from the declared dependency graph.
// Refining these thresholds - or replacing them with a weighted score - is future
// work, not a gap to hide: every result carries its own reason string so it stays
// explainable either way.
@Service
public class RiskClassificationService {

    public record Classification(ImpactLevel impactLevel, String reason) {
    }

    public Classification classify(int depth, DependencyCriticality criticality,
                                     FailureBehavior failureBehavior, boolean runtimeEvidence) {
        boolean direct = depth == 1;
        boolean failClose = failureBehavior == FailureBehavior.FAIL_CLOSE;
        boolean highOrCritical = criticality == DependencyCriticality.HIGH || criticality == DependencyCriticality.CRITICAL;

        if (direct && failClose && criticality == DependencyCriticality.CRITICAL) {
            return new Classification(ImpactLevel.CRITICAL,
                    describe("Direct fail-close dependency with critical criticality", runtimeEvidence));
        }
        if (direct && failClose && highOrCritical) {
            return new Classification(ImpactLevel.HIGH,
                    describe("Direct fail-close dependency with high criticality", runtimeEvidence));
        }
        if (direct && failClose) {
            return new Classification(ImpactLevel.HIGH, describe("Direct fail-close dependency", runtimeEvidence));
        }
        if (direct && criticality == DependencyCriticality.CRITICAL) {
            return new Classification(ImpactLevel.HIGH,
                    describe("Direct dependency with critical criticality", runtimeEvidence));
        }
        if (direct) {
            return new Classification(ImpactLevel.MEDIUM, describe("Direct dependency", runtimeEvidence));
        }
        if (criticality == DependencyCriticality.CRITICAL) {
            return new Classification(ImpactLevel.MEDIUM,
                    describe("Indirect dependency with critical criticality", runtimeEvidence));
        }
        return new Classification(ImpactLevel.LOW, describe("Indirect dependency", runtimeEvidence));
    }

    private String describe(String base, boolean runtimeEvidence) {
        return runtimeEvidence ? base + " (runtime confirmed)" : base;
    }
}
