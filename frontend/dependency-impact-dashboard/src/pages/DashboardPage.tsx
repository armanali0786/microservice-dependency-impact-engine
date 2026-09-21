import { useDashboardSummary } from "../features/dashboard/hooks/useDashboardSummary";
import { DashboardSummaryCards } from "../features/dashboard/components/DashboardSummaryCards";
import { ActiveIncidentsWidget } from "../features/dashboard/components/ActiveIncidentsWidget";
import { Skeleton } from "../components/ui/Skeleton";
import { ErrorState } from "../components/ui/ErrorState";

export function DashboardPage() {
  const summary = useDashboardSummary();

  if (summary.isLoading) return <Skeleton />;
  if (summary.isError) return <ErrorState error={summary.error} />;

  return (
    <div className="stack">
      <h1>Dashboard</h1>
      <DashboardSummaryCards
        serviceCount={summary.serviceCount}
        dependencyCount={summary.dependencyCount}
        activeIncidentCount={summary.activeIncidents.length}
      />
      <ActiveIncidentsWidget incidents={summary.activeIncidents} />
    </div>
  );
}
