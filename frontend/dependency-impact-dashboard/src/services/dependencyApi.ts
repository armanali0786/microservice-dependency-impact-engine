import { apiClient } from "./apiClient";
import { unwrap } from "../utils/apiResponse";
import type { ApiResponse } from "../types/apiResponse.types";
import type { Dependency } from "../types/dependency.types";

export const dependencyApi = {
  listDownstream: (serviceId: string, environment: string) =>
    apiClient
      .get<ApiResponse<Dependency[]>>(`/services/${serviceId}/dependencies`, { params: { environment } })
      .then(unwrap),

  listUpstream: (serviceId: string, environment: string) =>
    apiClient
      .get<ApiResponse<Dependency[]>>(`/services/${serviceId}/dependents`, { params: { environment } })
      .then(unwrap),

  listAll: () => apiClient.get<ApiResponse<Dependency[]>>("/dependencies").then(unwrap),
};
