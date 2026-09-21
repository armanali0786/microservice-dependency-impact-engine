import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { incidentApi } from "../../../services/incidentApi";
import { useEnvironmentStore } from "../../../store/environmentStore";

export function useTriggerBlastRadiusAnalysis(incidentId: string) {
  const queryClient = useQueryClient();
  const environment = useEnvironmentStore((state) => state.environment);
  return useMutation({
    mutationFn: () => incidentApi.analyzeImpact(incidentId, environment),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["incidents", incidentId] }),
  });
}

export function useIncidentBlastRadius(incidentId: string, enabled: boolean) {
  return useQuery({
    queryKey: ["incidents", incidentId, "impact"],
    queryFn: () => incidentApi.getImpact(incidentId),
    enabled,
    retry: false,
  });
}
