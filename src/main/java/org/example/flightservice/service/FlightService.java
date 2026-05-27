package org.example.flightservice.service;

import java.util.List;
import java.util.Optional;

import org.example.flightservice.exception.DuplicateFlightException;
import org.example.flightservice.model.Flight;
import org.example.flightservice.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flight save(Flight flight) {
        return flightRepository.save(normalize(flight));
    }

    public Flight create(Flight flight) {
        Flight normalized = normalize(flight);
        if (flightRepository.existsById(normalized.getCode())) {
            throw new DuplicateFlightException("Flight already exists with code " + normalized.getCode());
        }
        return flightRepository.save(normalized);
    }

    public Optional<Flight> update(String code, Flight flight) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        String normalizedCode = code.trim().toUpperCase();
        if (!flightRepository.existsById(normalizedCode)) {
            return Optional.empty();
        }

        Flight normalized = normalize(flight);
        normalized.setCode(normalizedCode);
        return Optional.of(flightRepository.save(normalized));
    }

    @Transactional(readOnly = true)
    public Optional<Flight> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return flightRepository.findById(code.trim().toUpperCase());
    }

    @Transactional(readOnly = true)
    public List<Flight> findByCarrier(String carrier) {
        return flightRepository.findByCarrierContainingIgnoreCaseOrderByCodeAsc(normalizeQuery(carrier));
    }

    @Transactional(readOnly = true)
    public List<Flight> findByRoute(String source, String destination) {
        return flightRepository.findBySourceContainingIgnoreCaseAndDestinationContainingIgnoreCaseOrderByCodeAsc(
                normalizeQuery(source),
                normalizeQuery(destination)
        );
    }

    @Transactional(readOnly = true)
    public List<Flight> findByPriceRange(double min, double max) {
        double lower = Math.min(min, max);
        double upper = Math.max(min, max);
        return flightRepository.findByCostBetweenOrderByCodeAsc(lower, upper);
    }

    @Transactional(readOnly = true)
    public List<Flight> list() {
        return flightRepository.findAll().stream()
                .sorted((first, second) -> first.getCode().compareTo(second.getCode()))
                .toList();
    }

    public boolean delete(String code) {
        if (code == null || code.isBlank()) {
            return false;
        }
        String normalizedCode = code.trim().toUpperCase();
        if (!flightRepository.existsById(normalizedCode)) {
            return false;
        }
        flightRepository.deleteById(normalizedCode);
        return true;
    }

    private Flight normalize(Flight flight) {
        return new Flight(
                flight.getCode().trim().toUpperCase(),
                flight.getCarrier().trim(),
                flight.getSource().trim(),
                flight.getDestination().trim(),
                flight.getCost()
        );
    }

    private String normalizeQuery(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
