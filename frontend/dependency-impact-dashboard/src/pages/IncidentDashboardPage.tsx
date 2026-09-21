import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useIncidents } from "../features/incidents/hooks/useIncidents";
import { useServices } from "../features/services/hooks/useServices";
import { Badge } from "../components/ui/Badge";
import { Select } from "../components/ui/Select";
import { Skeleton } from "../components/ui/Skeleton";
import { ErrorState } from "../components/ui/ErrorState";
import { EmptyState } from "../components/ui/EmptyState";
import { formatRelativeTime } from "../utils/formatting";
import { paths } from "../routes/paths";

const STATUSES = ["OPEN", "INVESTIGATING", "MITIGATED", "RESOLVED", "CLOSED"];
const SEVERITIES = ["LOW", "MEDIUM", "HIGH", "CRITICAL"];

export function IncidentDashboardPage() {
  const [status, setStatus] = useState("");
  const [severity, setSeverity] = useState("");
  const navigate = useNavigate();

  const incidents = useIncidents({ status: status || undefined, severity: severity || undefined });
  const services = useServices();
  const nameById = new Map((services.data ?? []).map((service) => [service.id, service.name]));

  if (incidents.isLoading) return <Skeleton />;
  if (incidents.isError) return <ErrorState error={incidents.error} />;

  return (
    <div className="stack">
      <h1>Incidents</h1>
      <div className="row">
        <Select value={status} onChange={(e) => setStatus(e.target.value)}>
          <option value="">All statuses</option>
          {STATUSES.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </Select>
        <Select value={severity} onChange={(e) => setSeverity(e.target.value)}>
          <option value="">All severities</option>
          {SEVERITIES.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </Select>
      </div>

      {(incidents.data ?? []).length === 0 ? (
        <EmptyState message="No incidents match these filters." />
      ) : (
        <table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Service</th>
              <th>Severity</th>
              <th>Status</th>
              <th>Started</th>
            </tr>
          </thead>
          <tbody>
            {(incidents.data ?? []).map((incident) => (
              <tr
                key={incident.id}
                className="table-row-link"
                onClick={() => navigate(paths.incidentDetail(incident.id))}
              >
                <td>{incident.title}</td>
                <td className="mono">{nameById.get(incident.serviceId) ?? incident.serviceId}</td>
                <td>
                  <Badge tone={incident.severity}>{incident.severity}</Badge>
                </td>
                <td>
                  <Badge tone={incident.status}>{incident.status}</Badge>
                </td>
                <td className="text-muted">{formatRelativeTime(incident.startedAt)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
