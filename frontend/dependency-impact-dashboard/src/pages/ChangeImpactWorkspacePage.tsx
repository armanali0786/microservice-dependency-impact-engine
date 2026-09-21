import { ImpactAnalysisForm } from "../features/impact/components/ImpactAnalysisForm";

export function ChangeImpactWorkspacePage() {
  return (
    <div className="stack">
      <h1>Change Impact Analysis</h1>
      <div className="card" style={{ maxWidth: 480 }}>
        <ImpactAnalysisForm />
      </div>
    </div>
  );
}
