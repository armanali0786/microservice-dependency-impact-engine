import { useState } from "react";
import { useParams } from "react-router-dom";
import { useService } from "../features/services/hooks/useService";
import { ServiceOverviewTab } from "../features/services/components/ServiceOverviewTab";
import { ServiceDependenciesTab } from "../features/services/components/ServiceDependenciesTab";
import { ServiceDependentsTab } from "../features/services/components/ServiceDependentsTab";
import { Tabs } from "../components/ui/Tabs";
import { Skeleton } from "../components/ui/Skeleton";
import { ErrorState } from "../components/ui/ErrorState";

const TAB_NAMES = ["Overview", "Dependencies", "Dependents"];

export function ServiceDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const [activeTab, setActiveTab] = useState("Overview");
  const service = useService(id!);

  if (service.isLoading) return <Skeleton />;
  if (service.isError || !service.data) return <ErrorState error={service.error} />;

  return (
    <div className="stack">
      <h1 className="mono">{service.data.name}</h1>
      <Tabs tabs={TAB_NAMES} active={activeTab} onChange={setActiveTab} />
      {activeTab === "Overview" && <ServiceOverviewTab service={service.data} />}
      {activeTab === "Dependencies" && <ServiceDependenciesTab serviceId={service.data.id} />}
      {activeTab === "Dependents" && <ServiceDependentsTab serviceId={service.data.id} />}
    </div>
  );
}
