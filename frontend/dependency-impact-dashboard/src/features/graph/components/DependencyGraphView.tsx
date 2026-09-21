import { useMemo } from "react";
import {
  Background,
  Controls,
  ReactFlow,
  type Edge,
  type Node,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";
import type { GraphResponse } from "../../../types/graph.types";

const COLUMN_WIDTH = 220;
const ROW_HEIGHT = 90;

// docs/ui-ux.md section 24 asks for zoom/pan/node-selection/expand-collapse -
// @xyflow/react gives zoom/pan/selection for free. Expand/collapse and the
// richer node/edge interaction panels (sections 26-27) are deferred; this is a
// read-only rendering of whatever depth/direction the caller already requested.
//
// Layout: a simple BFS-from-root column layout (nodes grouped by hop count from
// root, undirected - a node reached via either an upstream or downstream edge
// still lands in the right column). Not a force-directed layout; good enough for
// the bounded, small subgraphs this MVP's traversal limits produce.
function layoutGraph(graph: GraphResponse): { nodes: Node[]; edges: Edge[] } {
  const adjacency = new Map<string, Set<string>>();
  for (const node of graph.nodes) adjacency.set(node.id, new Set());
  for (const edge of graph.edges) {
    adjacency.get(edge.source)?.add(edge.target);
    adjacency.get(edge.target)?.add(edge.source);
  }

  const depthById = new Map<string, number>([[graph.root, 0]]);
  const queue = [graph.root];
  while (queue.length > 0) {
    const current = queue.shift()!;
    const currentDepth = depthById.get(current)!;
    for (const neighbor of adjacency.get(current) ?? []) {
      if (!depthById.has(neighbor)) {
        depthById.set(neighbor, currentDepth + 1);
        queue.push(neighbor);
      }
    }
  }

  const countByDepth = new Map<number, number>();
  const nodes: Node[] = graph.nodes.map((node) => {
    const depth = depthById.get(node.id) ?? 0;
    const row = countByDepth.get(depth) ?? 0;
    countByDepth.set(depth, row + 1);

    return {
      id: node.id,
      position: { x: depth * COLUMN_WIDTH, y: row * ROW_HEIGHT },
      data: { label: node.name },
      style: node.id === graph.root
        ? { border: "2px solid #5b8def", borderRadius: 6 }
        : { borderRadius: 6 },
    };
  });

  const edges: Edge[] = graph.edges.map((edge) => ({
    id: `${edge.source}-${edge.target}-${edge.type}`,
    source: edge.source,
    target: edge.target,
    label: edge.type,
    animated: edge.type === "KAFKA",
  }));

  return { nodes, edges };
}

interface DependencyGraphViewProps {
  graph: GraphResponse;
}

export function DependencyGraphView({ graph }: DependencyGraphViewProps) {
  const { nodes, edges } = useMemo(() => layoutGraph(graph), [graph]);

  return (
    <div style={{ height: 520, background: "#0b0d12", borderRadius: 6, border: "1px solid #262b36" }}>
      <ReactFlow nodes={nodes} edges={edges} fitView colorMode="dark">
        <Background />
        <Controls />
      </ReactFlow>
    </div>
  );
}
