import { useNavigate } from "react-router-dom";
import type { Service } from "../../../types/service.types";
import type { Team } from "../../../types/team.types";
import { Badge } from "../../../components/ui/Badge";
import { EmptyState } from "../../../components/ui/EmptyState";
import { paths } from "../../../routes/paths";

interface ServiceTableProps {
  services: Service[];
  teamsById: Map<string, Team>;
}

export function ServiceTable({ services, teamsById }: ServiceTableProps) {
  const navigate = useNavigate();

  if (services.length === 0) {
    return <EmptyState message="No services match your search." />;
  }

  return (
    <table>
      <thead>
        <tr>
          <th>Service</th>
          <th>Team</th>
          <th>Technology</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        {services.map((service) => (
          <tr
            key={service.id}
            className="table-row-link"
            onClick={() => navigate(paths.serviceDetail(service.id))}
          >
            <td className="mono">{service.name}</td>
            <td>{teamsById.get(service.teamId)?.name ?? "—"}</td>
            <td>{service.technology ?? "—"}</td>
            <td>
              <Badge tone={service.status}>{service.status}</Badge>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
