import { useParams } from "react-router-dom";
import { useIncident, useUpdateIncidentStatus } from "../features/incidents/hooks/useIncident";
import {
  useIncidentBlastRadius,
  useTriggerBlastRadiusAnalysis,
} from "../features/incidents/hooks/useIncidentBlastRadius";
import { IncidentBlastRadius } from "../features/incidents/components/IncidentBlastRadius";
import { Badge } from "../components/ui/Badge";
import { Select } from "../components/ui/Select";
import { Button } from "../components/ui/Button";
import { Skeleton } from "../components/ui/Skeleton";
import { ErrorState } from "../components/ui/ErrorState";
import { formatDateTime, formatLatency, formatPercent } from "../utils/formatting";

const STATUSES = ["OPEN", "INVESTIGATING", "MITIGATED", "RESOLVED", "CLOSED"];

export function IncidentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const incident = useIncident(id!);
  const updateStatus = useUpdateIncidentStatus(id!);
  const triggerAnalysis = useTriggerBlastRadiusAnalysis(id!);
  const blastRadius = useIncidentBlastRadius(id!, !!incident.data?.impactAnalysisId);

  if (incident.isLoading) return <Skeleton />;
  if (incident.isError || !incident.data) return <ErrorState error={incident.error} />;

  const data = incident.data;

  return (
    <div className="stack">
      <div className="page-header">
        <h1>{data.title}</h1>
        <Badge tone={data.severity}>{data.severity}</Badge>
      </div>

      <div className="card">
        <div className="row row--between">
          <span>
            Status:{" "}
            <Select
              value={data.status}
              onChange={(e) => updateStatus.mutate(e.target.value)}
              disabled={updateStatus.isPending}
            >
              {STATUSES.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </Select>
          </span>
          <span className="text-muted">Started {formatDateTime(data.startedAt)}</span>
        </div>
        {data.description && <p>{data.description}</p>}
        <div className="row">
          <span className="text-muted">Error rate: {formatPercent(data.errorRate)}</span>
          <span className="text-muted">Latency: {formatLatency(data.latencyMs)}</span>
        </div>
      </div>

      <div className="card">
        <div className="card__title">Blast Radius</div>
        {!data.impactAnalysisId && (
          <Button
            variant="primary"
            onClick={() => triggerAnalysis.mutate()}
            disabled={triggerAnalysis.isPending}
          >
            {triggerAnalysis.isPending ? "Starting…" : "Run blast-radius analysis"}
          </Button>
        )}
        {data.impactAnalysisId && blastRadius.isLoading && <Skeleton />}
        {data.impactAnalysisId && blastRadius.isError && (
          <div className="text-muted">Analysis is still running - try refreshing shortly.</div>
        )}
        {blastRadius.data && <IncidentBlastRadius impact={blastRadius.data} />}
      </div>
    </div>
  );
}
