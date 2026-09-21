import { EnvironmentSelector } from "./EnvironmentSelector";
import { UserProfileMenu } from "./UserProfileMenu";

export function TopNavigation() {
  return (
    <header className="top-nav">
      <div className="top-nav__brand">Dependency Impact Engine</div>
      <div className="top-nav__right">
        <EnvironmentSelector />
        <UserProfileMenu />
      </div>
    </header>
  );
}
