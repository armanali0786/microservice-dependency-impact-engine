export type DependencyType =
  | "REST"
  | "KAFKA"
  | "DATABASE"
  | "CACHE"
  | "QUEUE"
  | "EXTERNAL_API"
  | "RUNTIME";

export interface Dependency {
  id: string;
  sourceServiceId: string;
  targetServiceId: string;
  dependencyType: DependencyType;
  protocol: string | null;
  endpoint: string | null;
  topicName: string | null;
  environment: string;
  confidence: string | null;
  status: string;
  firstSeenAt: string;
  lastSeenAt: string;
}
