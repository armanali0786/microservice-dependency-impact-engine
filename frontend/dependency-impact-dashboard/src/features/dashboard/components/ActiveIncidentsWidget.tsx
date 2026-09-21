import { useNavigate } from "react-router-dom";
import type { Incident } from "../../../types/incident.types";
import { Card } from "../../../components/ui/Card";
import { Badge } from "../../../components/ui/Badge";
import { EmptyState } from "../../../components/ui/EmptyState";
import { formatRelativeTime } from "../../../utils/formatting";
import { paths } from "../../../routes/paths";

interface ActiveIncidentsWidgetProps {
  incidents: Incident[];
}

export function ActiveIncidentsWidget({ incidents }: ActiveIncidentsWidgetProps) {
  const navigate = useNavigate();

  return (
    <Card title="Active Incidents">
      {incidents.length === 0 ? (
        <EmptyState message="No active incidents." />
      ) : (
        <table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Severity</th>
              <th>Status</th>
              <th>Started</th>
            </tr>
          </thead>
          <tbody>
            {incidents.map((incident) => (
              <tr
                key={incident.id}
                className="table-row-link"
                onClick={() => navigate(paths.incidentDetail(incident.id))}
              >
                <td>{incident.title}</td>
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
    </Card>
  );
}
