import { Badge } from "../../../components/ui/Badge";
import type { ImpactLevel } from "../../../types/impactAnalysis.types";

interface RiskBadgeProps {
  level: ImpactLevel | null;
}

export function RiskBadge({ level }: RiskBadgeProps) {
  if (!level) return <span className="text-muted">—</span>;
  return <Badge tone={level}>{level}</Badge>;
}
