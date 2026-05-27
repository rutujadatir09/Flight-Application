import { List } from 'lucide-react';
import { activeSearchLabel } from '../helpers/flightFormatters';
import { FlightTable } from './FlightTable';

export function ResultsPanel({ activeSearch, error, flights, loading, message, onDelete, onEdit, onListAll }) {
  return (
    <section className="results">
      <div className="results-heading">
        <div>
          <span className="eyebrow">{activeSearchLabel(activeSearch)}</span>
          <h2>Flight List</h2>
        </div>
        <button type="button" onClick={onListAll} disabled={loading}>
          <List size={18} />
          List all
        </button>
      </div>

      {message && <p className="status success">{message}</p>}
      {error && <p className="status error">{error}</p>}

      <FlightTable flights={flights} loading={loading} onDelete={onDelete} onEdit={onEdit} />
    </section>
  );
}
