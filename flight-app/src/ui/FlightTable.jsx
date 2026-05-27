import { Pencil, Trash2 } from 'lucide-react';
import { formatCurrency } from '../helpers/flightFormatters';

export function FlightTable({ flights, loading, onDelete, onEdit }) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Code</th>
            <th>Carrier</th>
            <th>Source</th>
            <th>Destination</th>
            <th>Cost</th>
            <th aria-label="Actions"></th>
          </tr>
        </thead>
        <tbody>
          {flights.map((item) => (
            <tr key={item.code}>
              <td>{item.code}</td>
              <td>{item.carrier}</td>
              <td>{item.source}</td>
              <td>{item.destination}</td>
              <td>{formatCurrency(item.cost)}</td>
              <td>
                <div className="row-actions">
                  <button
                    className="edit-button"
                    type="button"
                    onClick={() => onEdit(item)}
                    disabled={loading}
                    title={`Edit ${item.code}`}
                  >
                    <Pencil size={16} />
                  </button>
                  <button
                    className="delete-button"
                    type="button"
                    onClick={() => onDelete(item.code)}
                    disabled={loading}
                    title={`Delete ${item.code}`}
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              </td>
            </tr>
          ))}
          {!flights.length && (
            <tr>
              <td className="empty-state" colSpan="6">
                No flights match the current criteria.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
