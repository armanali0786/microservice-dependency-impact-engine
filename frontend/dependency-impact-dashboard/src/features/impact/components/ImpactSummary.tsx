import { MetricCard } from "../../../components/ui/MetricCard";
import { RiskBadge } from "./RiskBadge";
import type { ImpactReport } from "../../../types/impactComponent.types";

interface ImpactSummaryProps {
  report: ImpactReport;
}

export function ImpactSummary({ report }: ImpactSummaryProps) {
  return (
    <div className="stack">
      <div className="row">
        <h2 className="mono">{report.sourceService}</h2>
        <RiskBadge level={report.riskLevel} />
      </div>
      <div className="metric-grid">
        <MetricCard label="Direct Consumers" value={report.summary.directConsumers} />
        <MetricCard label="Indirect Consumers" value={report.summary.indirectConsumers} />
        <MetricCard label="Affected Kafka Topics" value={report.summary.affectedKafkaTopics} />
      </div>
    </div>
  );
}
