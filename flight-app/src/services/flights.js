const API_BASE = import.meta.env.VITE_API_BASE_URL ?? '/api/flights';

export async function requestFlight(path = '', options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options
  });

  if (!response.ok) {
    const detail = await response.text();
    throw new Error(detail || `Request failed with status ${response.status}`);
  }

  if (response.status === 204) {
    return { ok: true };
  }

  return response.json();
}
