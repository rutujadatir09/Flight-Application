package org.example.flightservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.flightservice.model.Flight;
import org.example.flightservice.repository.FlightRepository;
import org.example.flightservice.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FlightServiceTest {
    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    void savesAndFindsFlightByCode() {
        flightService.save(new Flight("sg500", "SpiceJet", "Pune", "Goa", 3100));

        assertThat(flightService.findByCode("SG500"))
                .isPresent()
                .get()
                .extracting(Flight::getCarrier)
                .isEqualTo("SpiceJet");
    }

    @Test
    void findsFlightsByCarrierRouteAndPriceRange() {
        flightService.save(new Flight("QP777", "Akasa Air", "Delhi", "Goa", 4500));

        assertThat(flightService.findByCarrier("akasa")).extracting(Flight::getCode).contains("QP777");
        assertThat(flightService.findByRoute("del", "goa")).extracting(Flight::getCode).contains("QP777");
        assertThat(flightService.findByPriceRange(4000, 5000)).extracting(Flight::getCode).contains("QP777");
    }

    @Test
    void deletesFlightByCode() {
        flightService.save(new Flight("IX900", "Air India Express", "Kochi", "Dubai", 12000));

        assertThat(flightService.delete("ix900")).isTrue();
        assertThat(flightService.findByCode("IX900")).isEmpty();
    }
}
