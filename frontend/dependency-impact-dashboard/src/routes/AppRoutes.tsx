import { Navigate, Route, Routes } from "react-router-dom";
import { ProtectedRoute } from "./ProtectedRoute";
import { AuthLayout } from "../layouts/AuthLayout";
import { MainLayout } from "../layouts/MainLayout";
import { LoginPage } from "../pages/LoginPage";
import { DashboardPage } from "../pages/DashboardPage";
import { ServiceExplorerPage } from "../pages/ServiceExplorerPage";
import { ServiceDetailsPage } from "../pages/ServiceDetailsPage";
import { DependencyGraphPage } from "../pages/DependencyGraphPage";
import { ChangeImpactWorkspacePage } from "../pages/ChangeImpactWorkspacePage";
import { ImpactReportPage } from "../pages/ImpactReportPage";
import { IncidentDashboardPage } from "../pages/IncidentDashboardPage";
import { IncidentDetailsPage } from "../pages/IncidentDetailsPage";
import { NotFoundPage } from "../pages/NotFoundPage";
import { paths } from "./paths";

export function AppRoutes() {
  return (
    <Routes>
      <Route element={<AuthLayout />}>
        <Route path={paths.login} element={<LoginPage />} />
      </Route>

      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout />}>
          <Route path="/" element={<Navigate to={paths.dashboard} replace />} />
          <Route path={paths.dashboard} element={<DashboardPage />} />
          <Route path={paths.services} element={<ServiceExplorerPage />} />
          <Route path="/services/:id" element={<ServiceDetailsPage />} />
          <Route path="/graph/:id" element={<DependencyGraphPage />} />
          <Route path={paths.impactNew} element={<ChangeImpactWorkspacePage />} />
          <Route path="/impact/:id" element={<ImpactReportPage />} />
          <Route path={paths.incidents} element={<IncidentDashboardPage />} />
          <Route path="/incidents/:id" element={<IncidentDetailsPage />} />
        </Route>
      </Route>

      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}
