import { Outlet } from "react-router-dom";
import { TopNavigation } from "./TopNavigation";
import { Sidebar } from "./Sidebar";

export function AppShell() {
  return (
    <div className="app-shell">
      <div className="app-shell__topnav">
        <TopNavigation />
      </div>
      <div className="app-shell__sidebar">
        <Sidebar />
      </div>
      <main className="app-shell__main">
        <Outlet />
      </main>
    </div>
  );
}
