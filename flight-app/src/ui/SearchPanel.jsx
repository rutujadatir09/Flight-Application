import { Search } from 'lucide-react';
import { Field } from './Field';

export function SearchPanel({ filters, loading, onChange, onSearchByCarrier, onSearchByCode, onSearchByPrice, onSearchByRoute }) {
  return (
    <section className="panel search-panel">
      <div className="panel-heading">
        <Search size={20} />
        <h2>Find Flights</h2>
      </div>

      <div className="search-block">
        <Field label="Flight code" value={filters.code} onChange={(value) => onChange('code', value)} />
        <button type="button" onClick={onSearchByCode} disabled={loading}>
          Find by code
        </button>
      </div>

      <div className="search-block">
        <Field label="Carrier" value={filters.carrier} onChange={(value) => onChange('carrier', value)} />
        <button type="button" onClick={onSearchByCarrier} disabled={loading}>
          Find by carrier
        </button>
      </div>

      <div className="search-block two-columns">
        <Field label="Source" value={filters.source} onChange={(value) => onChange('source', value)} />
        <Field label="Destination" value={filters.destination} onChange={(value) => onChange('destination', value)} />
        <button type="button" onClick={onSearchByRoute} disabled={loading}>
          Find by route
        </button>
      </div>

      <div className="search-block two-columns">
        <Field label="Min cost" type="number" value={filters.min} onChange={(value) => onChange('min', value)} />
        <Field label="Max cost" type="number" value={filters.max} onChange={(value) => onChange('max', value)} />
        <button type="button" onClick={onSearchByPrice} disabled={loading}>
          Find by price
        </button>
      </div>
    </section>
  );
}
