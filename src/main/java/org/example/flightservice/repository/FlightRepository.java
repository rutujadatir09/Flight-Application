package org.example.flightservice.repository;

import java.util.List;

import org.example.flightservice.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, String> {
    List<Flight> findByCarrierContainingIgnoreCaseOrderByCodeAsc(String carrier);

    List<Flight> findBySourceContainingIgnoreCaseAndDestinationContainingIgnoreCaseOrderByCodeAsc(
            String source,
            String destination
    );

    List<Flight> findByCostBetweenOrderByCodeAsc(double min, double max);
}
