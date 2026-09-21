import { Link } from "react-router-dom";
import { paths } from "../routes/paths";

export function NotFoundPage() {
  return (
    <div className="empty-state">
      <p>Page not found.</p>
      <Link to={paths.dashboard}>Back to dashboard</Link>
    </div>
  );
}
