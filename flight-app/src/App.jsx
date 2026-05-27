import { useCallback, useEffect, useMemo, useState } from 'react';
import { requestFlight } from './services/flights';
import { FlightForm } from './ui/FlightForm';
import { ResultsPanel } from './ui/ResultsPanel';
import { SearchPanel } from './ui/SearchPanel';
import { StatsRail } from './ui/StatsRail';
import { TopBar } from './ui/TopBar';
import { emptyFilters, emptyFlight } from './config/forms';

export function App() {
  const [flights, setFlights] = useState([]);
  const [flight, setFlight] = useState(emptyFlight);
  const [editingCode, setEditingCode] = useState('');
  const [filters, setFilters] = useState(emptyFilters);
  const [activeSearch, setActiveSearch] = useState('list');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const totalRoutes = useMemo(() => {
    return new Set(flights.map((item) => `${item.source}-${item.destination}`)).size;
  }, [flights]);

  const run = useCallback(async (action, successMessage) => {
    setLoading(true);
    setError('');
    setMessage('');
    try {
      const result = await action();
      if (successMessage) {
        setMessage(successMessage);
      }
      return result;
    } catch (err) {
      setError(err.message || 'Something went wrong');
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  const loadFlights = useCallback(async (successMessage = 'Showing all flights') => {
    const data = await run(() => requestFlight(), successMessage);
    if (data) {
      setFlights(data);
      setActiveSearch('list');
    }
  }, [run]);

  useEffect(() => {
    loadFlights();
  }, [loadFlights]);

  async function saveFlight(event) {
    event.preventDefault();
    const payload = { ...flight, cost: Number(flight.cost) };
    const code = editingCode || flight.code;
    const method = editingCode ? 'PUT' : 'POST';
    const path = editingCode ? `/${encodeURIComponent(editingCode)}` : '';
    const success = editingCode ? `Updated flight ${code.toUpperCase()}` : `Saved flight ${code.toUpperCase()}`;
    const saved = await run(
      () => requestFlight(path, { method, body: JSON.stringify(payload) }),
      success
    );

    if (saved) {
      setFlight(emptyFlight);
      setEditingCode('');
      await loadFlights(success);
    }
  }

  async function searchByCode() {
    if (!filters.code.trim()) {
      setError('Enter a flight code');
      return;
    }

    const data = await run(() => requestFlight(`/${encodeURIComponent(filters.code)}`), 'Found matching flight');
    if (data) {
      setFlights([data]);
      setActiveSearch('code');
    }
  }

  async function searchByCarrier() {
    if (!filters.carrier.trim()) {
      setError('Enter a carrier name');
      return;
    }

    const data = await run(
      () => requestFlight(`/carrier/${encodeURIComponent(filters.carrier)}`),
      'Filtered by carrier'
    );
    if (data) {
      setFlights(data);
      setActiveSearch('carrier');
    }
  }

  async function searchByRoute() {
    if (!filters.source.trim() || !filters.destination.trim()) {
      setError('Enter both source and destination');
      return;
    }

    const params = new URLSearchParams({
      source: filters.source,
      destination: filters.destination
    });
    const data = await run(() => requestFlight(`/route?${params}`), 'Filtered by route');
    if (data) {
      setFlights(data);
      setActiveSearch('route');
    }
  }

  async function searchByPriceRange() {
    if (filters.min === '' || filters.max === '') {
      setError('Enter minimum and maximum cost');
      return;
    }

    const params = new URLSearchParams({ min: filters.min, max: filters.max });
    const data = await run(() => requestFlight(`/price?${params}`), 'Filtered by price range');
    if (data) {
      setFlights(data);
      setActiveSearch('price');
    }
  }

  async function deleteFlight(code) {
    const deleted = await run(() => requestFlight(`/${encodeURIComponent(code)}`, { method: 'DELETE' }), `Deleted ${code}`);
    if (deleted?.ok) {
      if (editingCode === code) {
        cancelEdit();
      }
      await loadFlights();
    }
  }

  function editFlight(item) {
    setEditingCode(item.code);
    setFlight({
      code: item.code,
      carrier: item.carrier,
      source: item.source,
      destination: item.destination,
      cost: String(item.cost)
    });
    setMessage(`Editing flight ${item.code}`);
    setError('');
  }

  function cancelEdit() {
    setEditingCode('');
    setFlight(emptyFlight);
    setMessage('');
    setError('');
  }

  function updateFlight(field, value) {
    setFlight((current) => ({ ...current, [field]: value }));
  }

  function updateFilter(field, value) {
    setFilters((current) => ({ ...current, [field]: value }));
  }

  return (
    <main className="app-shell">
      <TopBar loading={loading} onRefresh={loadFlights} />
      <StatsRail flights={flights} totalRoutes={totalRoutes} />

      <section className="workspace">
        <FlightForm
          editingCode={editingCode}
          flight={flight}
          loading={loading}
          onCancel={cancelEdit}
          onChange={updateFlight}
          onSubmit={saveFlight}
        />
        <SearchPanel
          filters={filters}
          loading={loading}
          onChange={updateFilter}
          onSearchByCarrier={searchByCarrier}
          onSearchByCode={searchByCode}
          onSearchByPrice={searchByPriceRange}
          onSearchByRoute={searchByRoute}
        />
      </section>

      <ResultsPanel
        activeSearch={activeSearch}
        error={error}
        flights={flights}
        loading={loading}
        message={message}
        onDelete={deleteFlight}
        onEdit={editFlight}
        onListAll={loadFlights}
      />
    </main>
  );
}
