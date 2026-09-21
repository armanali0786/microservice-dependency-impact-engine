import type { ReactNode } from "react";

interface BadgeProps {
  tone: string;
  children: ReactNode;
}

// Generic semantic badge - tone is lowercased and used directly as the
// badge--{tone} CSS class, so any backend enum value (status, severity, risk
// level) maps straight to a color without a switch statement per call site.
export function Badge({ tone, children }: BadgeProps) {
  return <span className={`badge badge--${tone.toLowerCase()}`}>{children}</span>;
}
