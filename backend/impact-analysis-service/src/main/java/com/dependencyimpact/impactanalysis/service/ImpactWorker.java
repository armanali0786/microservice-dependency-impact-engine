package com.dependencyimpact.impactanalysis.service;

import com.dependencyimpact.common.model.DependencyType;
import com.dependencyimpact.common.model.ImpactLevel;
import com.dependencyimpact.impactanalysis.client.GraphResponse;
import com.dependencyimpact.impactanalysis.client.GraphServiceClient;
import com.dependencyimpact.impactanalysis.client.RuntimeAnalysisClient;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisCompletedPayload;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisFailedPayload;
import com.dependencyimpact.impactanalysis.entity.ImpactAnalysis;
import com.dependencyimpact.impactanalysis.entity.ImpactComponent;
import com.dependencyimpact.impactanalysis.exception.ImpactAnalysisNotFoundException;
import com.dependencyimpact.impactanalysis.kafka.producer.ImpactAnalysisEventProducer;
import com.dependencyimpact.impactanalysis.mapper.ImpactComponentMapper;
import com.dependencyimpact.impactanalysis.repository.ImpactAnalysisRepository;
import com.dependencyimpact.impactanalysis.repository.ImpactComponentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// This is the "Impact Worker" from docs/api-structure.md section 91: the thing that
// actually does the expensive work, off the request thread, triggered by the Kafka
// consumer after IMPACT_ANALYSIS_REQUESTED. Runtime evidence collection is a no-op
// today (RuntimeAnalysisClient stub) - see that class for why.
@Service
public class ImpactWorker {

    private static final Logger log = LoggerFactory.getLogger(ImpactWorker.class);

    private final ImpactAnalysisRepository impactAnalysisRepository;
    private final ImpactComponentRepository impactComponentRepository;
    private final GraphServiceClient graphServiceClient;
    private final RuntimeAnalysisClient runtimeAnalysisClient;
    private final DependencyPathBuilder dependencyPathBuilder;
    private final RiskClassificationService riskClassificationService;
    private final ImpactComponentMapper impactComponentMapper;
    private final ImpactAnalysisEventProducer eventProducer;

    public ImpactWorker(ImpactAnalysisRepository impactAnalysisRepository,
                         ImpactComponentRepository impactComponentRepository,
                         GraphServiceClient graphServiceClient,
                         RuntimeAnalysisClient runtimeAnalysisClient,
                         DependencyPathBuilder dependencyPathBuilder,
                         RiskClassificationService riskClassificationService,
                         ImpactComponentMapper impactComponentMapper,
                         ImpactAnalysisEventProducer eventProducer) {
        this.impactAnalysisRepository = impactAnalysisRepository;
        this.impactComponentRepository = impactComponentRepository;
        this.graphServiceClient = graphServiceClient;
        this.runtimeAnalysisClient = runtimeAnalysisClient;
        this.dependencyPathBuilder = dependencyPathBuilder;
        this.riskClassificationService = riskClassificationService;
        this.impactComponentMapper = impactComponentMapper;
        this.eventProducer = eventProducer;
    }

    public void process(UUID analysisId) {
        ImpactAnalysis analysis = impactAnalysisRepository.findById(analysisId)
                .orElseThrow(() -> new ImpactAnalysisNotFoundException("Impact analysis not found: " + analysisId));

        analysis.setStatus("RUNNING");
        analysis.setStartedAt(Instant.now());
        impactAnalysisRepository.save(analysis);

        try {
            GraphResponse graph = graphServiceClient.getUpstreamGraph(
                    analysis.getSourceServiceId(), analysis.getTraversalDepth(), analysis.getEnvironment());

            List<TraversedComponent> traversed = dependencyPathBuilder.build(graph);

            // ImpactLevel is declared LOW..CRITICAL in ascending severity, so the
            // highest ordinal seen across all components is the analysis' overall risk.
            ImpactLevel overallRisk = ImpactLevel.LOW;
            List<ImpactComponent> componentsToSave = new ArrayList<>();
            for (TraversedComponent component : traversed) {
                boolean runtimeEvidence = runtimeAnalysisClient.hasRuntimeEvidence(
                        analysis.getSourceServiceId(), component.serviceId());

                RiskClassificationService.Classification classification = riskClassificationService.classify(
                        component.depth(), component.criticality(), component.failureBehavior(), runtimeEvidence);

                componentsToSave.add(impactComponentMapper.toEntity(
                        analysisId, component, classification.impactLevel(), classification.reason(), runtimeEvidence));

                if (classification.impactLevel().ordinal() > overallRisk.ordinal()) {
                    overallRisk = classification.impactLevel();
                }
            }
            impactComponentRepository.saveAll(componentsToSave);

            analysis.setStatus("COMPLETED");
            analysis.setRiskLevel(overallRisk.name());
            analysis.setCompletedAt(Instant.now());
            impactAnalysisRepository.save(analysis);

            long kafkaTopics = traversed.stream()
                    .filter(c -> c.dependencyType() == DependencyType.KAFKA)
                    .count();

            eventProducer.publishCompleted(new ImpactAnalysisCompletedPayload(
                    analysisId, overallRisk.name(), traversed.size(), 0, (int) kafkaTopics), analysis.getEnvironment());

        } catch (Exception e) {
            log.error("Impact analysis {} failed", analysisId, e);
            analysis.setStatus("FAILED");
            analysis.setErrorMessage(e.getMessage());
            analysis.setCompletedAt(Instant.now());
            impactAnalysisRepository.save(analysis);
            eventProducer.publishFailed(new ImpactAnalysisFailedPayload(analysisId, e.getMessage()), analysis.getEnvironment());
        }
    }
}
