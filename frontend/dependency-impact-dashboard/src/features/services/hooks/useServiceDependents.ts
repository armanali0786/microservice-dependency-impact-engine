import { useQuery } from "@tanstack/react-query";
import { dependencyApi } from "../../../services/dependencyApi";
import { useEnvironmentStore } from "../../../store/environmentStore";

export function useServiceDependents(serviceId: string) {
  const environment = useEnvironmentStore((state) => state.environment);
  return useQuery({
    queryKey: ["services", serviceId, "dependents", environment],
    queryFn: () => dependencyApi.listUpstream(serviceId, environment),
  });
}
