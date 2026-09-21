import { useMutation } from "@tanstack/react-query";
import { impactApi } from "../../../services/impactApi";
import type { StartImpactAnalysisRequest } from "../../../types/impactAnalysis.types";

export function useRunImpactAnalysis() {
  return useMutation({
    mutationFn: (request: StartImpactAnalysisRequest) => impactApi.analyze(request),
  });
}
