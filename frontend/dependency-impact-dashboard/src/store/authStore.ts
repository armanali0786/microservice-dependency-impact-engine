import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { CurrentUser, Role } from "../types/auth.types";

interface AuthState {
  accessToken: string | null;
  user: CurrentUser | null;
  login: (accessToken: string, user: CurrentUser) => void;
  logout: () => void;
  hasRole: (...roles: Role[]) => boolean;
}

// Persisted to localStorage so a page refresh doesn't force a re-login - the token
// itself still carries its own expiry, so this is a convenience, not a trust
// decision (apiClient's 401 handler clears it the moment the backend rejects it).
export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      accessToken: null,
      user: null,
      login: (accessToken, user) => set({ accessToken, user }),
      logout: () => set({ accessToken: null, user: null }),
      hasRole: (...roles) => {
        const user = get().user;
        return user != null && roles.includes(user.roles[0]);
      },
    }),
    { name: "dependency-impact-auth" },
  ),
);
