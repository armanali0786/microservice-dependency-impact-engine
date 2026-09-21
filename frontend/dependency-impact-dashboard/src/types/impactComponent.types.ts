import type { ImpactLevel } from "./impactAnalysis.types";

export interface ImpactComponent {
  serviceId: string;
  service: string;
  depth: number;
  impactLevel: ImpactLevel;
  reason: string;
  dependencyType: string | null;
  criticalDependency: boolean;
  runtimeEvidence: boolean;
  dependencyPath: string[];
}

export interface ImpactSummary {
  directConsumers: number;
  indirectConsumers: number;
  affectedKafkaTopics: number;
}

export interface ImpactReport {
  analysisId: string;
  sourceService: string;
  riskLevel: ImpactLevel | null;
  summary: ImpactSummary;
  components: ImpactComponent[];
}
