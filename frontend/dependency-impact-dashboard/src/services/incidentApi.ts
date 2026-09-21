import { apiClient } from "./apiClient";
import { unwrap } from "../utils/apiResponse";
import type { ApiResponse } from "../types/apiResponse.types";
import type { ImpactAnalysisResponse } from "../types/impactAnalysis.types";
import type { CreateIncidentRequest, Incident, IncidentImpactResponse } from "../types/incident.types";

export interface IncidentListFilters {
  serviceId?: string;
  severity?: string;
  status?: string;
}

export const incidentApi = {
  list: (filters: IncidentListFilters = {}) =>
    apiClient.get<ApiResponse<Incident[]>>("/incidents", { params: filters }).then(unwrap),

  get: (id: string) => apiClient.get<ApiResponse<Incident>>(`/incidents/${id}`).then(unwrap),

  create: (request: CreateIncidentRequest) =>
    apiClient.post<ApiResponse<Incident>>("/incidents", request).then(unwrap),

  updateStatus: (id: string, status: string) =>
    apiClient.patch<ApiResponse<Incident>>(`/incidents/${id}/status`, { status }).then(unwrap),

  analyzeImpact: (id: string, environment: string) =>
    apiClient
      .post<ApiResponse<ImpactAnalysisResponse>>(`/incidents/${id}/analyze-impact`, null, { params: { environment } })
      .then(unwrap),

  getImpact: (id: string) =>
    apiClient.get<ApiResponse<IncidentImpactResponse>>(`/incidents/${id}/impact`).then(unwrap),
};
