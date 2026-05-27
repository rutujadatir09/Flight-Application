export function averageCost(flights) {
  if (!flights.length) {
    return formatCurrency(0);
  }

  const total = flights.reduce((sum, flight) => sum + Number(flight.cost), 0);
  return formatCurrency(total / flights.length);
}

export function formatCurrency(value) {
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    maximumFractionDigits: 0
  }).format(value);
}

export function activeSearchLabel(search) {
  const labels = {
    list: 'All records',
    code: 'Code search',
    carrier: 'Carrier search',
    route: 'Route search',
    price: 'Price search'
  };

  return labels[search] ?? 'Results';
}
