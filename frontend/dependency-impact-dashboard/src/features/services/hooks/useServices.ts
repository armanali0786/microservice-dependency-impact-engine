import { useQuery } from "@tanstack/react-query";
import { serviceApi } from "../../../services/serviceApi";

export function useServices() {
  return useQuery({ queryKey: ["services"], queryFn: serviceApi.list });
}
