package org.example.flightservice;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.flightservice.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FlightControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    void createsListsSearchesAndDeletesFlights() throws Exception {
        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "code": "G8123",
                                  "carrier": "Go First",
                                  "source": "Ahmedabad",
                                  "destination": "Jaipur",
                                  "cost": 3900
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("G8123"));

        mockMvc.perform(get("/api/flights/G8123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carrier").value("Go First"));

        mockMvc.perform(get("/api/flights/carrier/Go"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].code", hasItem("G8123")));

        mockMvc.perform(get("/api/flights/route")
                        .param("source", "Ahmedabad")
                        .param("destination", "Jaipur"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].code", hasItem("G8123")));

        mockMvc.perform(get("/api/flights/price")
                        .param("min", "3000")
                        .param("max", "4000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].code", hasItem("G8123")));

        mockMvc.perform(delete("/api/flights/G8123"))
                .andExpect(status().isNoContent());
    }
}
