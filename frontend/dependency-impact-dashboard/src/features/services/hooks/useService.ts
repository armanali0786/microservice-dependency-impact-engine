import { useQuery } from "@tanstack/react-query";
import { serviceApi } from "../../../services/serviceApi";

export function useService(id: string) {
  return useQuery({ queryKey: ["services", id], queryFn: () => serviceApi.get(id) });
}
