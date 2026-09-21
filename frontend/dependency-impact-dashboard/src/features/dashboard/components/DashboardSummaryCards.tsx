import { MetricCard } from "../../../components/ui/MetricCard";

interface DashboardSummaryCardsProps {
  serviceCount: number;
  dependencyCount: number;
  activeIncidentCount: number;
}

export function DashboardSummaryCards({
  serviceCount,
  dependencyCount,
  activeIncidentCount,
}: DashboardSummaryCardsProps) {
  return (
    <div className="metric-grid">
      <MetricCard label="Services" value={serviceCount} />
      <MetricCard label="Dependencies" value={dependencyCount} />
      <MetricCard label="Active Incidents" value={activeIncidentCount} />
    </div>
  );
}
