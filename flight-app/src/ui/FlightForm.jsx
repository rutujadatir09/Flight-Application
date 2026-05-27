import { Loader2, Pencil, Plus, X } from 'lucide-react';
import { Field } from './Field';

export function FlightForm({ editingCode, flight, loading, onCancel, onChange, onSubmit }) {
  return (
    <form className="panel" onSubmit={onSubmit}>
      <div className="panel-heading">
        {editingCode ? <Pencil size={20} /> : <Plus size={20} />}
        <h2>{editingCode ? 'Update Flight' : 'Save Flight'}</h2>
      </div>

      <div className="form-grid">
        <Field
          label="Code"
          value={flight.code}
          onChange={(value) => onChange('code', value)}
          disabled={Boolean(editingCode)}
          required
        />
        <Field label="Carrier" value={flight.carrier} onChange={(value) => onChange('carrier', value)} required />
        <Field label="Source" value={flight.source} onChange={(value) => onChange('source', value)} required />
        <Field
          label="Destination"
          value={flight.destination}
          onChange={(value) => onChange('destination', value)}
          required
        />
        <Field
          label="Cost"
          type="number"
          min="1"
          step="0.01"
          value={flight.cost}
          onChange={(value) => onChange('cost', value)}
          required
        />
      </div>

      <div className="form-actions">
        <button className="primary-button" type="submit" disabled={loading}>
          {loading ? <Loader2 className="spin" size={18} /> : editingCode ? <Pencil size={18} /> : <Plus size={18} />}
          {editingCode ? 'Update' : 'Save'}
        </button>
        {editingCode && (
          <button className="secondary-button" type="button" onClick={onCancel} disabled={loading}>
            <X size={18} />
            Cancel
          </button>
        )}
      </div>
    </form>
  );
}
