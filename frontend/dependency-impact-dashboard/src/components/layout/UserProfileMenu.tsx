import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../store/authStore";
import { paths } from "../../routes/paths";

export function UserProfileMenu() {
  const user = useAuthStore((state) => state.user);
  const logout = useAuthStore((state) => state.logout);
  const navigate = useNavigate();

  if (!user) return null;

  return (
    <div className="row">
      <span className="text-muted">
        {user.fullName} <span className="mono">({user.roles[0]})</span>
      </span>
      <button
        className="btn"
        onClick={() => {
          logout();
          navigate(paths.login);
        }}
      >
        Log out
      </button>
    </div>
  );
}
