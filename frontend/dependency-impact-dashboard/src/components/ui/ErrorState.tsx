interface ErrorStateProps {
  error: unknown;
}

export function ErrorState({ error }: ErrorStateProps) {
  const message = error instanceof Error ? error.message : "Something went wrong.";
  return <div className="error-state">{message}</div>;
}
