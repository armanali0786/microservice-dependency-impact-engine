interface ImpactPathProps {
  path: string[];
}

// docs/ui-ux.md section 34: the dependency path is the core explainability
// device - render it as an explicit chain, not just a count.
export function ImpactPath({ path }: ImpactPathProps) {
  return (
    <div className="dependency-path">
      {path.map((step, index) => (
        <span key={`${step}-${index}`}>
          {index > 0 && <span className="dependency-path__arrow">→</span>} {step}
        </span>
      ))}
    </div>
  );
}
