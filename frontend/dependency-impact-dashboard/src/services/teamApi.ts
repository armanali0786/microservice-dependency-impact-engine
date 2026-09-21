import { apiClient } from "./apiClient";
import { unwrap } from "../utils/apiResponse";
import type { ApiResponse } from "../types/apiResponse.types";
import type { Team } from "../types/team.types";

export const teamApi = {
  list: () => apiClient.get<ApiResponse<Team[]>>("/teams").then(unwrap),
};
