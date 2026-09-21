import type { ReactNode } from "react";

interface CardProps {
  title?: string;
  children: ReactNode;
}

export function Card({ title, children }: CardProps) {
  return (
    <div className="card">
      {title && <div className="card__title">{title}</div>}
      {children}
    </div>
  );
}
