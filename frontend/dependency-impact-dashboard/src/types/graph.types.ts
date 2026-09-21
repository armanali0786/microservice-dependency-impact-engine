import type { DependencyType } from "./dependency.types";

export type NodeType =
  | "SERVICE"
  | "API"
  | "DATABASE"
  | "KAFKA_TOPIC"
  | "CACHE"
  | "QUEUE"
  | "EXTERNAL_API";

export type DependencyCriticality = "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
export type FailureBehavior = "FAIL_OPEN" | "FAIL_CLOSE" | "UNKNOWN";

export interface GraphNode {
  id: string;
  type: NodeType;
  name: string;
}

export interface GraphEdge {
  source: string;
  target: string;
  type: DependencyType;
  criticality: DependencyCriticality | null;
  failureBehavior: FailureBehavior | null;
}

export interface GraphResponse {
  root: string;
  nodes: GraphNode[];
  edges: GraphEdge[];
}

export type GraphDirection = "downstream" | "upstream" | "both";
