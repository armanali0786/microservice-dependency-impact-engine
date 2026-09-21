import type { ButtonHTMLAttributes } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: "default" | "primary" | "danger";
}

export function Button({ variant = "default", className, ...rest }: ButtonProps) {
  const variantClass = variant === "default" ? "" : ` btn--${variant}`;
  return <button className={`btn${variantClass} ${className ?? ""}`} {...rest} />;
}
