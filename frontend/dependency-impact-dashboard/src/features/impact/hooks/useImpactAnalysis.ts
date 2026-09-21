import { useQuery } from "@tanstack/react-query";
import { impactApi } from "../../../services/impactApi";

// Polls while the analysis is still PENDING/RUNNING (docs/kafka-spec.md section 68:
// large analyses should expose progress rather than block the request) and stops
// once it lands on a terminal status.
export function useImpactAnalysis(id: string) {
  return useQuery({
    queryKey: ["impact", id],
    queryFn: () => impactApi.getStatus(id),
    refetchInterval: (query) => {
      const status = query.state.data?.status;
      return status === "PENDING" || status === "RUNNING" ? 1500 : false;
    },
  });
}
