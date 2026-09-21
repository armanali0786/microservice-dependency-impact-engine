import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useServices } from "../../services/hooks/useServices";
import { useRunImpactAnalysis } from "../hooks/useRunImpactAnalysis";
import { useEnvironmentStore } from "../../../store/environmentStore";
import { Select } from "../../../components/ui/Select";
import { Input } from "../../../components/ui/Input";
import { Button } from "../../../components/ui/Button";
import { paths } from "../../../routes/paths";
import type { ChangeType } from "../../../types/impactAnalysis.types";

const CHANGE_TYPES: ChangeType[] = [
  "SERVICE_CHANGE",
  "API_CHANGE",
  "API_SCHEMA_CHANGE",
  "KAFKA_SCHEMA_CHANGE",
  "DATABASE_SCHEMA_CHANGE",
  "DEPLOYMENT",
  "CONFIGURATION_CHANGE",
  "FAILURE",
];

export function ImpactAnalysisForm() {
  const services = useServices();
  const environment = useEnvironmentStore((state) => state.environment);
  const runAnalysis = useRunImpactAnalysis();
  const navigate = useNavigate();

  const [serviceId, setServiceId] = useState("");
  const [changeType, setChangeType] = useState<ChangeType>("API_SCHEMA_CHANGE");
  const [depth, setDepth] = useState(2);

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    runAnalysis.mutate(
      { serviceId, changeType, environment, traversalDepth: depth },
      { onSuccess: (response) => navigate(paths.impactDetail(response.analysisId)) },
    );
  }

  return (
    <form className="stack" onSubmit={handleSubmit}>
      <div className="form-field">
        <label htmlFor="service">Service</label>
        <Select id="service" value={serviceId} onChange={(e) => setServiceId(e.target.value)} required>
          <option value="" disabled>
            Select a service…
          </option>
          {(services.data ?? []).map((service) => (
            <option key={service.id} value={service.id}>
              {service.name}
            </option>
          ))}
        </Select>
      </div>

      <div className="form-field">
        <label htmlFor="changeType">Change type</label>
        <Select id="changeType" value={changeType} onChange={(e) => setChangeType(e.target.value as ChangeType)}>
          {CHANGE_TYPES.map((type) => (
            <option key={type} value={type}>
              {type}
            </option>
          ))}
        </Select>
      </div>

      <div className="form-field">
        <label htmlFor="depth">Traversal depth</label>
        <Input
          id="depth"
          type="number"
          min={1}
          max={5}
          value={depth}
          onChange={(e) => setDepth(Number(e.target.value))}
        />
      </div>

      <div className="text-muted">Environment: {environment}</div>

      {runAnalysis.isError && <div className="error-state">{(runAnalysis.error as Error).message}</div>}

      <Button type="submit" variant="primary" disabled={!serviceId || runAnalysis.isPending}>
        {runAnalysis.isPending ? "Starting…" : "Run impact analysis"}
      </Button>
    </form>
  );
}
