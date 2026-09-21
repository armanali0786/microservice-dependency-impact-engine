export type ServiceStatus = "ACTIVE" | "INACTIVE" | "DEPRECATED" | "UNKNOWN";

export interface Service {
  id: string;
  teamId: string;
  name: string;
  description: string | null;
  technology: string | null;
  repositoryUrl: string | null;
  repositoryName: string | null;
  status: ServiceStatus;
  version: string | null;
  createdAt: string;
  updatedAt: string;
}
