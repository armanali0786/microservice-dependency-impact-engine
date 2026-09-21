import { Badge } from "../../../components/ui/Badge";
import type { ImpactAnalysisStatusResponse } from "../../../types/impactAnalysis.types";
import { formatDateTime } from "../../../utils/formatting";

interface ImpactAnalysisProgressProps {
  status: ImpactAnalysisStatusResponse;
}

export function ImpactAnalysisProgress({ status }: ImpactAnalysisProgressProps) {
  return (
    <div className="row">
      <Badge tone={status.status}>{status.status}</Badge>
      {status.startedAt && <span className="text-muted">started {formatDateTime(status.startedAt)}</span>}
      {status.errorMessage && <span className="error-state">{status.errorMessage}</span>}
    </div>
  );
}
