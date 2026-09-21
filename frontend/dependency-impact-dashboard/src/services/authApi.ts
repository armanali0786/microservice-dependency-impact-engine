import { apiClient } from "./apiClient";
import { unwrap } from "../utils/apiResponse";
import type { ApiResponse } from "../types/apiResponse.types";
import type { CurrentUser, LoginRequest, LoginResponse } from "../types/auth.types";

export const authApi = {
  login: (request: LoginRequest) =>
    apiClient
      .post<ApiResponse<LoginResponse>>("/auth/login", request)
      .then((res) => {
        console.log("[DEBUG] raw axios response received", res.status);
        return unwrap(res);
      })
      .catch((err) => {
        console.log("[DEBUG] authApi.login caught", err?.message, err);
        throw err;
      }),

  getCurrentUser: () =>
    apiClient.get<ApiResponse<CurrentUser>>("/auth/me").then(unwrap),
};
