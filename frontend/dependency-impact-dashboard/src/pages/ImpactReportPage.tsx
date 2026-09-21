import { useParams } from "react-router-dom";
import { useImpactAnalysis } from "../features/impact/hooks/useImpactAnalysis";
import { useImpactReport } from "../features/impact/hooks/useImpactReport";
import { ImpactAnalysisProgress } from "../features/impact/components/ImpactAnalysisProgress";
import { ImpactSummary } from "../features/impact/components/ImpactSummary";
import { RiskBadge } from "../features/impact/components/RiskBadge";
import { ImpactPath } from "../features/impact/components/ImpactPath";
import { Skeleton } from "../components/ui/Skeleton";
import { ErrorState } from "../components/ui/ErrorState";
import { EmptyState } from "../components/ui/EmptyState";

export function ImpactReportPage() {
  const { id } = useParams<{ id: string }>();
  const status = useImpactAnalysis(id!);
  const isCompleted = status.data?.status === "COMPLETED";
  const report = useImpactReport(id!, isCompleted);

  if (status.isLoading) return <Skeleton />;
  if (status.isError || !status.data) return <ErrorState error={status.error} />;

  return (
    <div className="stack">
      <h1>Impact Analysis</h1>
      <ImpactAnalysisProgress status={status.data} />

      {status.data.status === "FAILED" && (
        <ErrorState error={new Error(status.data.errorMessage ?? "Analysis failed.")} />
      )}

      {isCompleted && report.isLoading && <Skeleton />}
      {isCompleted && report.isError && <ErrorState error={report.error} />}

      {isCompleted && report.data && (
        <div className="stack">
          <ImpactSummary report={report.data} />
          <div className="card">
            <div className="card__title">Affected Components</div>
            {report.data.components.length === 0 ? (
              <EmptyState message="No affected components found - this service has no known consumers." />
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Service</th>
                    <th>Depth</th>
                    <th>Impact</th>
                    <th>Reason</th>
                    <th>Dependency Path</th>
                  </tr>
                </thead>
                <tbody>
                  {report.data.components.map((component) => (
                    <tr key={component.serviceId}>
                      <td className="mono">{component.service}</td>
                      <td>{component.depth}</td>
                      <td>
                        <RiskBadge level={component.impactLevel} />
                      </td>
                      <td>{component.reason}</td>
                      <td>
                        <ImpactPath path={component.dependencyPath} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
