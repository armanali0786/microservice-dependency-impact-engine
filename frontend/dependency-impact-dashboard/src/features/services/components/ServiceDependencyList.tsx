import { useNavigate } from "react-router-dom";
import type { Dependency } from "../../../types/dependency.types";
import { useServices } from "../hooks/useServices";
import { EmptyState } from "../../../components/ui/EmptyState";
import { paths } from "../../../routes/paths";

interface ServiceDependencyListProps {
  dependencies: Dependency[];
  // Which side of each row is "the other service" to display and link to -
  // for downstream deps that's the target, for upstream (dependents) it's the source.
  otherServiceKey: "targetServiceId" | "sourceServiceId";
  emptyMessage: string;
}

export function ServiceDependencyList({
  dependencies,
  otherServiceKey,
  emptyMessage,
}: ServiceDependencyListProps) {
  const navigate = useNavigate();
  const services = useServices();
  const nameById = new Map((services.data ?? []).map((service) => [service.id, service.name]));

  if (dependencies.length === 0) {
    return <EmptyState message={emptyMessage} />;
  }

  return (
    <table>
      <thead>
        <tr>
          <th>Service</th>
          <th>Type</th>
          <th>Protocol / Topic</th>
          <th>Confidence</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        {dependencies.map((dependency) => {
          const otherId = dependency[otherServiceKey];
          return (
            <tr
              key={dependency.id}
              className="table-row-link"
              onClick={() => navigate(paths.serviceDetail(otherId))}
            >
              <td>{nameById.get(otherId) ?? otherId}</td>
              <td>{dependency.dependencyType}</td>
              <td className="mono">{dependency.topicName ?? dependency.protocol ?? "—"}</td>
              <td>{dependency.confidence ?? "—"}</td>
              <td>{dependency.status}</td>
            </tr>
          );
        })}
      </tbody>
    </table>
  );
}
