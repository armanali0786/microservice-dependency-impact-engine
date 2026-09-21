export type IncidentSeverity = "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
export type IncidentStatus = "OPEN" | "INVESTIGATING" | "MITIGATED" | "RESOLVED" | "CLOSED";

export interface Incident {
  id: string;
  serviceId: string;
  title: string;
  description: string | null;
  severity: IncidentSeverity;
  status: IncidentStatus;
  errorRate: number | null;
  latencyMs: number | null;
  kafkaLag: number | null;
  startedAt: string;
  resolvedAt: string | null;
  impactAnalysisId: string | null;
}

export interface CreateIncidentRequest {
  serviceId: string;
  title: string;
  description?: string;
  severity: IncidentSeverity;
}

export interface IncidentImpactResponse {
  incidentId: string;
  affectedServices: string[];
  dependencyPaths: string[][];
}
