import { useServiceDependencies } from "../hooks/useServiceDependencies";
import { ServiceDependencyList } from "./ServiceDependencyList";
import { Skeleton } from "../../../components/ui/Skeleton";
import { ErrorState } from "../../../components/ui/ErrorState";

interface ServiceDependenciesTabProps {
  serviceId: string;
}

export function ServiceDependenciesTab({ serviceId }: ServiceDependenciesTabProps) {
  const dependencies = useServiceDependencies(serviceId);

  if (dependencies.isLoading) return <Skeleton />;
  if (dependencies.isError) return <ErrorState error={dependencies.error} />;

  return (
    <ServiceDependencyList
      dependencies={dependencies.data ?? []}
      otherServiceKey="targetServiceId"
      emptyMessage="This service doesn't depend on anything (in this environment)."
    />
  );
}
