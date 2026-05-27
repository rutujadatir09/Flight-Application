package org.example.flightservice.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.example.flightservice.exception.FlightNotFoundException;
import org.example.flightservice.model.Flight;
import org.example.flightservice.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:5173}")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public ResponseEntity<Flight> save(@Valid @RequestBody Flight flight) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightService.create(flight));
    }

    @PutMapping("/{code}")
    public Flight update(@PathVariable String code, @Valid @RequestBody Flight flight) {
        return flightService.update(code, flight)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with code " + code));
    }

    @GetMapping
    public List<Flight> list() {
        return flightService.list();
    }

    @GetMapping("/{code}")
    public Flight findByCode(@PathVariable String code) {
        return flightService.findByCode(code)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with code " + code));
    }

    @GetMapping("/carrier/{carrier}")
    public List<Flight> findByCarrier(@PathVariable String carrier) {
        return flightService.findByCarrier(carrier);
    }

    @GetMapping("/route")
    public List<Flight> findByRoute(@RequestParam String source, @RequestParam String destination) {
        return flightService.findByRoute(source, destination);
    }

    @GetMapping("/price")
    public List<Flight> findByPriceRange(@RequestParam double min, @RequestParam double max) {
        return flightService.findByPriceRange(min, max);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        if (!flightService.delete(code)) {
            throw new FlightNotFoundException("Flight not found with code " + code);
        }
        return ResponseEntity.noContent().build();
    }
}
