package org.example.flightservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @NotBlank(message = "Flight code is required")
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @NotBlank(message = "Carrier is required")
    @Column(name = "carrier", nullable = false, length = 100)
    private String carrier;

    @NotBlank(message = "Source is required")
    @Column(name = "source", nullable = false, length = 100)
    private String source;

    @NotBlank(message = "Destination is required")
    @Column(name = "destination", nullable = false, length = 100)
    private String destination;

    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than zero")
    @Column(name = "cost", nullable = false)
    private double cost;

    public Flight() {
    }

    public Flight(String code, String carrier, String source, String destination, double cost) {
        this.code = code;
        this.carrier = carrier;
        this.source = source;
        this.destination = destination;
        this.cost = cost;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
