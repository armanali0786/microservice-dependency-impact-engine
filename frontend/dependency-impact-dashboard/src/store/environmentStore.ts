import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { Environment } from "../types/environment.types";

interface EnvironmentState {
  environment: Environment;
  setEnvironment: (environment: Environment) => void;
}

export const useEnvironmentStore = create<EnvironmentState>()(
  persist(
    (set) => ({
      environment: "development",
      setEnvironment: (environment) => set({ environment }),
    }),
    { name: "dependency-impact-environment" },
  ),
);
