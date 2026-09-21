import { useServiceDependents } from "../hooks/useServiceDependents";
import { ServiceDependencyList } from "./ServiceDependencyList";
import { Skeleton } from "../../../components/ui/Skeleton";
import { ErrorState } from "../../../components/ui/ErrorState";

interface ServiceDependentsTabProps {
  serviceId: string;
}

export function ServiceDependentsTab({ serviceId }: ServiceDependentsTabProps) {
  const dependents = useServiceDependents(serviceId);

  if (dependents.isLoading) return <Skeleton />;
  if (dependents.isError) return <ErrorState error={dependents.error} />;

  return (
    <ServiceDependencyList
      dependencies={dependents.data ?? []}
      otherServiceKey="sourceServiceId"
      emptyMessage="Nothing depends on this service (in this environment)."
    />
  );
}
