import { useQuery } from "@tanstack/react-query";
import { impactApi } from "../../../services/impactApi";

export function useImpactReport(id: string, enabled: boolean) {
  return useQuery({
    queryKey: ["impact", id, "report"],
    queryFn: () => impactApi.getReport(id),
    enabled,
  });
}
