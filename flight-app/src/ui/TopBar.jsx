import { RefreshCw } from 'lucide-react';

export function TopBar({ loading, onRefresh }) {
  return (
    <header className="topbar">
      <div>
        <span className="eyebrow">Flight Service</span>
        <h1>Flight App</h1>
      </div>
      <button className="icon-button" type="button" onClick={onRefresh} disabled={loading} title="Refresh flights">
        <RefreshCw size={18} />
        Refresh
      </button>
    </header>
  );
}
