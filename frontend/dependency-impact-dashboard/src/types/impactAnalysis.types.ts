export type ChangeType =
  | "SERVICE_CHANGE"
  | "API_CHANGE"
  | "API_SCHEMA_CHANGE"
  | "KAFKA_SCHEMA_CHANGE"
  | "DATABASE_SCHEMA_CHANGE"
  | "DEPLOYMENT"
  | "CONFIGURATION_CHANGE"
  | "FAILURE";

export type ImpactAnalysisStatus = "PENDING" | "RUNNING" | "COMPLETED" | "FAILED" | "CANCELLED";
export type ImpactLevel = "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";

export interface StartImpactAnalysisRequest {
  serviceId: string;
  changeType: ChangeType;
  environment: string;
  traversalDepth?: number;
}

export interface ImpactAnalysisResponse {
  analysisId: string;
  status: ImpactAnalysisStatus;
}

export interface ImpactAnalysisStatusResponse {
  id: string;
  status: ImpactAnalysisStatus;
  riskLevel: ImpactLevel | null;
  startedAt: string | null;
  completedAt: string | null;
  errorMessage: string | null;
}
