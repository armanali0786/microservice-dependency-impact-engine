import type { AxiosResponse } from "axios";
import type { ApiResponse } from "../types/apiResponse.types";

// Every endpoint wraps its payload in {success, data, error, meta} - this unwraps
// it once, in one place, so every api/*.ts call site can just await a plain value
// and let React Query's error path carry the backend's own message instead of a
// generic Axios error.
export function unwrap<T>(response: AxiosResponse<ApiResponse<T>>): T {
  const body = response.data;
  if (!body.success || body.data === null) {
    throw new Error(body.error?.message ?? "Request failed");
  }
  return body.data;
}
