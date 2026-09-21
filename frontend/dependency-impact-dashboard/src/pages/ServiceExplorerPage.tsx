import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { useServices } from "../features/services/hooks/useServices";
import { teamApi } from "../services/teamApi";
import { ServiceTable } from "../features/services/components/ServiceTable";
import { Input } from "../components/ui/Input";
import { Skeleton } from "../components/ui/Skeleton";
import { ErrorState } from "../components/ui/ErrorState";

export function ServiceExplorerPage() {
  const [search, setSearch] = useState("");
  const services = useServices();
  const teams = useQuery({ queryKey: ["teams"], queryFn: teamApi.list });

  if (services.isLoading) return <Skeleton />;
  if (services.isError) return <ErrorState error={services.error} />;

  const teamsById = new Map((teams.data ?? []).map((team) => [team.id, team]));
  const filtered = (services.data ?? []).filter((service) =>
    service.name.toLowerCase().includes(search.toLowerCase()),
  );

  return (
    <div className="stack">
      <div className="page-header">
        <h1>Services</h1>
      </div>
      <Input
        placeholder="Search services…"
        value={search}
        onChange={(event) => setSearch(event.target.value)}
      />
      <ServiceTable services={filtered} teamsById={teamsById} />
    </div>
  );
}
