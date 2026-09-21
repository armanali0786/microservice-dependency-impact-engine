import { apiClient } from "./apiClient";
import { unwrap } from "../utils/apiResponse";
import type { ApiResponse } from "../types/apiResponse.types";
import type { GraphDirection, GraphResponse } from "../types/graph.types";

const PATH_SUFFIX: Record<GraphDirection, string> = {
  both: "",
  downstream: "/downstream",
  upstream: "/upstream",
};

export const graphApi = {
  get: (serviceId: string, direction: GraphDirection, depth: number, environment: string) =>
    apiClient
      .get<ApiResponse<GraphResponse>>(`/graph/services/${serviceId}${PATH_SUFFIX[direction]}`, {
        params: { depth, environment },
      })
      .then(unwrap),
};
