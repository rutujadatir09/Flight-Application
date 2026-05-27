package org.example.flightservice.config;

import org.example.flightservice.model.Flight;
import org.example.flightservice.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class FlightDataSeeder {
    @Bean
    CommandLineRunner seedFlights(FlightRepository flightRepository) {
        return args -> {
            if (flightRepository.count() > 0) {
                return;
            }

            flightRepository.save(new Flight("AI101", "Air India", "Delhi", "Mumbai", 5500));
            flightRepository.save(new Flight("6E204", "IndiGo", "Bengaluru", "Delhi", 6200));
            flightRepository.save(new Flight("UK302", "Vistara", "Mumbai", "Chennai", 4800));
        };
    }
}
