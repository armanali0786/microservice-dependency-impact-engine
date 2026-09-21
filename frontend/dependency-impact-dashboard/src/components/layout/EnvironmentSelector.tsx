import { ENVIRONMENTS } from "../../types/environment.types";
import { useEnvironmentStore } from "../../store/environmentStore";

export function EnvironmentSelector() {
  const environment = useEnvironmentStore((state) => state.environment);
  const setEnvironment = useEnvironmentStore((state) => state.setEnvironment);

  return (
    <select
      className="env-select"
      value={environment}
      onChange={(event) => setEnvironment(event.target.value as typeof environment)}
    >
      {ENVIRONMENTS.map((env) => (
        <option key={env} value={env}>
          {env[0].toUpperCase() + env.slice(1)}
        </option>
      ))}
    </select>
  );
}
