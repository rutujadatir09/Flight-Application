import { BadgeIndianRupee, Plane, Route } from 'lucide-react';
import { averageCost } from '../helpers/flightFormatters';
import { SummaryItem } from './SummaryItem';

export function StatsRail({ flights, totalRoutes }) {
  return (
    <section className="stats-grid" aria-label="Flight summary">
      <SummaryItem icon={<Plane size={20} />} label="Flights" value={flights.length} />
      <SummaryItem icon={<Route size={20} />} label="Routes" value={totalRoutes} />
      <SummaryItem icon={<BadgeIndianRupee size={20} />} label="Average cost" value={averageCost(flights)} />
    </section>
  );
}
