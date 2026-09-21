import { useQuery } from "@tanstack/react-query";
import { serviceApi } from "../../../services/serviceApi";
import { dependencyApi } from "../../../services/dependencyApi";
import { incidentApi } from "../../../services/incidentApi";

// service-registry's DashboardController is an unimplemented backend stub (no
// aggregation endpoint exists yet), so this composes the summary client-side from
// three existing list endpoints instead - reasonable for this MVP's dataset size,
// and avoids opening new backend scope for what's meant to be the frontend phase.
export function useDashboardSummary() {
  const services = useQuery({ queryKey: ["services"], queryFn: serviceApi.list });
  const dependencies = useQuery({ queryKey: ["dependencies"], queryFn: dependencyApi.listAll });
  const incidents = useQuery({ queryKey: ["incidents"], queryFn: () => incidentApi.list() });

  return {
    isLoading: services.isLoading || dependencies.isLoading || incidents.isLoading,
    isError: services.isError || dependencies.isError || incidents.isError,
    error: services.error ?? dependencies.error ?? incidents.error,
    serviceCount: services.data?.length ?? 0,
    dependencyCount: dependencies.data?.length ?? 0,
    activeIncidents: (incidents.data ?? []).filter(
      (incident) => incident.status === "OPEN" || incident.status === "INVESTIGATING",
    ),
  };
}
