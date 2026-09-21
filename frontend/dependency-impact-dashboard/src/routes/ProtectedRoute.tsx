import { Navigate, Outlet } from "react-router-dom";
import { useAuthStore } from "../store/authStore";
import { paths } from "./paths";

export function ProtectedRoute() {
  const accessToken = useAuthStore((state) => state.accessToken);

  if (!accessToken) {
    return <Navigate to={paths.login} replace />;
  }

  return <Outlet />;
}
