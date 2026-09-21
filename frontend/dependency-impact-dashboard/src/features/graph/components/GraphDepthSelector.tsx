import { Select } from "../../../components/ui/Select";
import type { GraphDirection } from "../../../types/graph.types";

interface GraphDepthSelectorProps {
  direction: GraphDirection;
  onDirectionChange: (direction: GraphDirection) => void;
  depth: number;
  onDepthChange: (depth: number) => void;
}

export function GraphDepthSelector({
  direction,
  onDirectionChange,
  depth,
  onDepthChange,
}: GraphDepthSelectorProps) {
  return (
    <div className="row">
      <Select value={direction} onChange={(event) => onDirectionChange(event.target.value as GraphDirection)}>
        <option value="both">Both directions</option>
        <option value="downstream">Downstream (what it calls)</option>
        <option value="upstream">Upstream (what calls it)</option>
      </Select>
      <Select value={depth} onChange={(event) => onDepthChange(Number(event.target.value))}>
        {[1, 2, 3, 4, 5].map((value) => (
          <option key={value} value={value}>
            Depth {value}
          </option>
        ))}
      </Select>
    </div>
  );
}
