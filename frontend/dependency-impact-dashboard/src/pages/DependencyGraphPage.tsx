import { useState } from "react";
import { useParams } from "react-router-dom";
import { useDependencyGraph } from "../features/graph/hooks/useDependencyGraph";
import { DependencyGraphView } from "../features/graph/components/DependencyGraphView";
import { GraphDepthSelector } from "../features/graph/components/GraphDepthSelector";
import { Skeleton } from "../components/ui/Skeleton";
import { ErrorState } from "../components/ui/ErrorState";
import type { GraphDirection } from "../types/graph.types";

export function DependencyGraphPage() {
  const { id } = useParams<{ id: string }>();
  const [direction, setDirection] = useState<GraphDirection>("both");
  const [depth, setDepth] = useState(2);
  const graph = useDependencyGraph(id!, direction, depth);

  return (
    <div className="stack">
      <div className="page-header">
        <h1>Dependency Graph</h1>
        <GraphDepthSelector
          direction={direction}
          onDirectionChange={setDirection}
          depth={depth}
          onDepthChange={setDepth}
        />
      </div>
      {graph.isLoading && <Skeleton />}
      {graph.isError && <ErrorState error={graph.error} />}
      {graph.data && <DependencyGraphView graph={graph.data} />}
    </div>
  );
}
