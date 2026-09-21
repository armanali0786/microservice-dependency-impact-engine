import { apiClient } from "./apiClient";
import { unwrap } from "../utils/apiResponse";
import type { ApiResponse } from "../types/apiResponse.types";
import type {
  ImpactAnalysisResponse,
  ImpactAnalysisStatusResponse,
  StartImpactAnalysisRequest,
} from "../types/impactAnalysis.types";
import type { ImpactReport } from "../types/impactComponent.types";

export const impactApi = {
  analyze: (request: StartImpactAnalysisRequest) =>
    apiClient.post<ApiResponse<ImpactAnalysisResponse>>("/impact/analyze", request).then(unwrap),

  getStatus: (id: string) =>
    apiClient.get<ApiResponse<ImpactAnalysisStatusResponse>>(`/impact/${id}`).then(unwrap),

  getReport: (id: string) =>
    apiClient.get<ApiResponse<ImpactReport>>(`/impact/${id}/report`).then(unwrap),
};
