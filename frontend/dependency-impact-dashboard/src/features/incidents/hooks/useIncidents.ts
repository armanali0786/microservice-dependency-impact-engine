import { useQuery } from "@tanstack/react-query";
import { incidentApi, type IncidentListFilters } from "../../../services/incidentApi";

export function useIncidents(filters: IncidentListFilters = {}) {
  return useQuery({
    queryKey: ["incidents", filters],
    queryFn: () => incidentApi.list(filters),
  });
}
