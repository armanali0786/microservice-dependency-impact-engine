import { useMutation } from "@tanstack/react-query";
import { authApi } from "../../../services/authApi";
import { useAuthStore } from "../../../store/authStore";
import type { LoginRequest } from "../../../types/auth.types";

export function useLogin() {
  const login = useAuthStore((state) => state.login);

  return useMutation({
    mutationFn: (request: LoginRequest) => authApi.login(request),
    onSuccess: (response) => {
      console.log("[DEBUG] useLogin onSuccess, setting store", response.accessToken.slice(0, 10));
      login(response.accessToken, response.user);
      console.log("[DEBUG] store after login()", useAuthStore.getState().accessToken?.slice(0, 10));
    },
    onError: (err) => {
      console.log("[DEBUG] useLogin onError", err);
    },
  });
}
