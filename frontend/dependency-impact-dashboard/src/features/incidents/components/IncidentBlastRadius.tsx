import type { IncidentImpactResponse } from "../../../types/incident.types";
import { ImpactPath } from "../../impact/components/ImpactPath";
import { EmptyState } from "../../../components/ui/EmptyState";

interface IncidentBlastRadiusProps {
  impact: IncidentImpactResponse;
}

export function IncidentBlastRadius({ impact }: IncidentBlastRadiusProps) {
  if (impact.affectedServices.length === 0) {
    return <EmptyState message="No affected services found." />;
  }

  return (
    <div className="stack">
      <div>
        <strong>Affected services: </strong>
        {impact.affectedServices.join(", ")}
      </div>
      <div className="stack">
        {impact.dependencyPaths.map((path, index) => (
          <ImpactPath key={index} path={path} />
        ))}
      </div>
    </div>
  );
}
