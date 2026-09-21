// Mirrors com.dependencyimpact.common.model.ApiResponse/ApiError - every backend
// endpoint responds in this envelope, per docs/api-structure.md.
export interface ApiError {
  code: string;
  message: string;
  details: Record<string, unknown> | null;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T | null;
  error: ApiError | null;
  meta: Record<string, unknown> | null;
}
