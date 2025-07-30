package nz.co.acmeair.flight.service.impl;

import nz.co.acmeair.flight.data.FlightDataProvider;
import nz.co.acmeair.flight.model.FlightRequest;
import nz.co.acmeair.flight.model.FlightResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FlightServiceImplTest {

    private FlightServiceImpl service;
    
    private FlightDataProvider mockProvider;

    @BeforeEach
    void setUp() {
    	
        mockProvider = mock(FlightDataProvider.class);
        
        service = new FlightServiceImpl(mockProvider);
        
    }

    @Test
    void searchAvailableFlightsSuccess() {
    	
        FlightResponse flight = new FlightResponse("A4075D0814","A4075D0814", "Auckland", "Bali",  "2025-08-14", 10, 860);
        
        when(mockProvider.getAllFlights()).thenReturn(List.of(flight));

        FlightRequest req = new FlightRequest("Auckland", "Bali", "2025-08-14", 1);
        
        req = new FlightRequest(req.from(), req.to(), req.date(), req.passengers());
        
        Optional<List<FlightResponse>> result = service.searchAvailableFlights(req);

        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(1);
        assertThat(result.get().get(0).getFlightId()).isEqualTo("A4075D0814");
        
    }

    @Test
    void searchAvailableFlightsEmpty() {
    	
        when(mockProvider.getAllFlights()).thenReturn(List.of());
        
        FlightRequest req = new FlightRequest("Auckland", "Bali", "2025-08-14", 1);
        
        Optional<List<FlightResponse>> result = service.searchAvailableFlights(req);
        
        assertThat(result).isEmpty();
        
    }

    @Test
    void searchFlightByIdSuccess() {
    	
        FlightResponse flight = new FlightResponse("A4075D0814","A4075D0814", "Auckland", "Bali",  "2025-08-14", 10, 860);
        
        when(mockProvider.getFlightById("A4075D0814")).thenReturn(Optional.of(flight));
        
        Optional<FlightResponse> result = service.searchFlightById("A4075D0814");
        
        assertThat(result).isPresent();
        assertThat(result.get().getFlightId()).isEqualTo("A4075D0814");
    }

    @Test
    void searchFlightByIdNotFound() {
    	
        when(mockProvider.getFlightById("QQ9988")).thenReturn(Optional.empty());
        
        Optional<FlightResponse> result = service.searchFlightById("QQ9988");
        
        assertThat(result).isEmpty();
        
    }
}
