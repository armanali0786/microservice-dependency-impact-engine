import { apiClient } from "./apiClient";
import { unwrap } from "../utils/apiResponse";
import type { ApiResponse } from "../types/apiResponse.types";
import type { Service } from "../types/service.types";

export const serviceApi = {
  list: () => apiClient.get<ApiResponse<Service[]>>("/services").then(unwrap),

  get: (id: string) => apiClient.get<ApiResponse<Service>>(`/services/${id}`).then(unwrap),
};
