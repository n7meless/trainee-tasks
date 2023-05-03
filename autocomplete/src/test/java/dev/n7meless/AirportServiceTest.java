package dev.n7meless;

import dev.n7meless.service.AirportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AirportServiceTest {
    private final AirportService airportService = new AirportService();

    @Test
    public void readFileWithBadFilter() {
        Assertions.assertDoesNotThrow(() -> airportService.readFileWithFilter("][';.;"));
    }
    @Test
    public void readFileWithFilter() {
        Assertions.assertDoesNotThrow(() -> airportService.findByAirportName("]a['\'s"));
    }
}