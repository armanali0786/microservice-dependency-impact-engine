import { NavLink } from "react-router-dom";
import { paths } from "../../routes/paths";

// Groups follow docs/ui-ux.md section 8's nav structure, but only link to pages
// that are actually built in this pass - APIs/Kafka/Databases/External
// Dependencies/Teams/Users/Audit Logs are scaffolded on the backend and/or
// frontend but not wired up yet, so they're deliberately left off rather than
// linking to a page that renders nothing.
const linkClass = ({ isActive }: { isActive: boolean }) =>
  `sidebar__link${isActive ? " sidebar__link--active" : ""}`;

export function Sidebar() {
  return (
    <nav className="sidebar">
      <div className="sidebar__group">
        <NavLink to={paths.dashboard} className={linkClass}>
          Dashboard
        </NavLink>
      </div>

      <div className="sidebar__group">
        <div className="sidebar__group-label">Architecture</div>
        <NavLink to={paths.services} className={linkClass}>
          Services
        </NavLink>
      </div>

      <div className="sidebar__group">
        <div className="sidebar__group-label">Analysis</div>
        <NavLink to={paths.impactNew} className={linkClass}>
          Change Impact
        </NavLink>
        <NavLink to={paths.incidents} className={linkClass}>
          Incidents
        </NavLink>
      </div>
    </nav>
  );
}
