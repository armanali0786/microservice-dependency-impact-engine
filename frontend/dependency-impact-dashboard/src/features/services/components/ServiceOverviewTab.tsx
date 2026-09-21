import { Link } from "react-router-dom";
import type { Service } from "../../../types/service.types";
import { Badge } from "../../../components/ui/Badge";
import { formatDateTime } from "../../../utils/formatting";
import { paths } from "../../../routes/paths";

interface ServiceOverviewTabProps {
  service: Service;
}

export function ServiceOverviewTab({ service }: ServiceOverviewTabProps) {
  return (
    <div className="stack">
      <div className="row">
        <Badge tone={service.status}>{service.status}</Badge>
        {service.technology && <span className="text-muted">{service.technology}</span>}
        {service.version && <span className="mono text-muted">v{service.version}</span>}
      </div>
      {service.description && <p>{service.description}</p>}
      <table>
        <tbody>
          <tr>
            <th>Repository</th>
            <td>
              {service.repositoryUrl ? (
                <a href={service.repositoryUrl} target="_blank" rel="noreferrer">
                  {service.repositoryName ?? service.repositoryUrl}
                </a>
              ) : (
                "—"
              )}
            </td>
          </tr>
          <tr>
            <th>Created</th>
            <td>{formatDateTime(service.createdAt)}</td>
          </tr>
          <tr>
            <th>Updated</th>
            <td>{formatDateTime(service.updatedAt)}</td>
          </tr>
        </tbody>
      </table>
      <Link to={paths.graph(service.id)}>View dependency graph →</Link>
    </div>
  );
}
