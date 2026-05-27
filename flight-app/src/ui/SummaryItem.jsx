export function SummaryItem({ icon, label, value }) {
  return (
    <article className="summary-item">
      <div className="summary-icon">{icon}</div>
      <div>
        <span>{label}</span>
        <strong>{value}</strong>
      </div>
    </article>
  );
}
