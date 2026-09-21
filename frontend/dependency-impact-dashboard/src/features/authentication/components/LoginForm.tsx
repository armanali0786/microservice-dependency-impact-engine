import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useLogin } from "../hooks/useLogin";
import { Input } from "../../../components/ui/Input";
import { Button } from "../../../components/ui/Button";
import { paths } from "../../../routes/paths";

export function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const login = useLogin();
  const navigate = useNavigate();

  function handleSubmit(event: React.FormEvent) {
    (window as any).__CLICKED = true;
    console.log("[DEBUG] handleSubmit fired");
    event.preventDefault();
    console.log("[DEBUG] calling mutate with", email, password.length);
    login.mutate(
      { email, password },
      {
        onSuccess: () => {
          console.log("[DEBUG] call-level onSuccess, navigating");
          navigate(paths.dashboard);
        },
      },
    );
  }

  return (
    <form className="stack" onSubmit={handleSubmit}>
      <h2>Sign in</h2>
      <div className="form-field">
        <label htmlFor="email">Email</label>
        <Input
          id="email"
          type="email"
          autoComplete="username"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          required
        />
      </div>
      <div className="form-field">
        <label htmlFor="password">Password</label>
        <Input
          id="password"
          type="password"
          autoComplete="current-password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          required
        />
      </div>
      {login.isError && <div className="error-state">{(login.error as Error).message}</div>}
      <button
        type="button"
        id="raw-test-button"
        onClick={() => {
          (window as any).__RAW_CLICK = true;
          console.log("[DEBUG] raw button onClick fired");
        }}
      >
        RAW TEST BUTTON
      </button>
      <Button type="submit" variant="primary" disabled={login.isPending}>
        {login.isPending ? "Signing in…" : "Sign in"}
      </Button>
    </form>
  );
}
