import { useQuery } from "@tanstack/react-query";
import { graphApi } from "../../../services/graphApi";
import { useEnvironmentStore } from "../../../store/environmentStore";
import type { GraphDirection } from "../../../types/graph.types";

export function useDependencyGraph(serviceId: string, direction: GraphDirection, depth: number) {
  const environment = useEnvironmentStore((state) => state.environment);
  return useQuery({
    queryKey: ["graph", serviceId, direction, depth, environment],
    queryFn: () => graphApi.get(serviceId, direction, depth, environment),
  });
}
