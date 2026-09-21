export const paths = {
  login: "/login",
  dashboard: "/dashboard",
  services: "/services",
  serviceDetail: (id: string) => `/services/${id}`,
  graph: (id: string) => `/graph/${id}`,
  impactNew: "/impact/new",
  impactDetail: (id: string) => `/impact/${id}`,
  incidents: "/incidents",
  incidentDetail: (id: string) => `/incidents/${id}`,
};
