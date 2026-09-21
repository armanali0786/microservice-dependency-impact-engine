import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useMutation } from "@tanstack/react-query";
import { incidentApi } from "../../../services/incidentApi";

export function useIncident(id: string) {
  return useQuery({ queryKey: ["incidents", id], queryFn: () => incidentApi.get(id) });
}

export function useUpdateIncidentStatus(id: string) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (status: string) => incidentApi.updateStatus(id, status),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["incidents", id] }),
  });
}
