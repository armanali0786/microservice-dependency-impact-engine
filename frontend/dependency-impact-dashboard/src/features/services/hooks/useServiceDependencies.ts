import { useQuery } from "@tanstack/react-query";
import { dependencyApi } from "../../../services/dependencyApi";
import { useEnvironmentStore } from "../../../store/environmentStore";

export function useServiceDependencies(serviceId: string) {
  const environment = useEnvironmentStore((state) => state.environment);
  return useQuery({
    queryKey: ["services", serviceId, "dependencies", environment],
    queryFn: () => dependencyApi.listDownstream(serviceId, environment),
  });
}
