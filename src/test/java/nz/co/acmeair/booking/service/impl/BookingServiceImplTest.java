package nz.co.acmeair.booking.service.impl;

import nz.co.acmeair.booking.data.BookingData;
import nz.co.acmeair.booking.model.BookingRequest;
import nz.co.acmeair.booking.model.BookingResponse;
import nz.co.acmeair.flight.model.FlightResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {

    private RestTemplate restTemplate;
    private BookingData bookingData;
    private BookingServiceImpl service;

    @BeforeEach
    void setUp() {
    	
        restTemplate = mock(RestTemplate.class);
        bookingData = mock(BookingData.class);
        
        service = new BookingServiceImpl(bookingData, restTemplate);
        
        ReflectionTestUtils.setField(service, "flightServiceUrl", "http://localhost:8080/api/public/v1/flight/");

    }

    @Test
    void createBookingSuccess() {
    	
        BookingRequest req = new BookingRequest("A4075D0814", "Mercedeh", 2);
        FlightResponse flight = new FlightResponse("A4075D0814", "A4075", "Auckland", "Bali", "2025-08-14", 10, 800);

        when(restTemplate.getForObject("http://localhost:8080/api/public/v1/flight/A4075D0814", FlightResponse.class))
                .thenReturn(flight);

        Optional<BookingResponse> result = service.createBooking(req);

        assertThat(result).isPresent();
        assertThat(result.get().passengerName()).isEqualTo("Mercedeh");
        assertThat(result.get().flightId()).isEqualTo("A4075D0814");
        assertThat(result.get().numberOfSeats()).isEqualTo(2);
        assertThat(result.get().totalPrice()).isEqualTo(1600);
                
    }

    @Test
    void createBookingFlightNotFound() {
    	
        BookingRequest req = new BookingRequest("INVALID", "Mercedeh", 1);

        when(restTemplate.getForObject(anyString(), eq(FlightResponse.class))).thenThrow(new RuntimeException());

        Optional<BookingResponse> result = service.createBooking(req);

        assertThat(result).isEmpty();
    }

    @Test
    void createBookingNotEnoughSeats() {
    	
        BookingRequest req = new BookingRequest("A4075D0814", "Mercedeh", 10);
        FlightResponse flight = new FlightResponse("A4075D0814", "A4075", "Auckland", "Bali", "2025-08-14", 5, 800);

        when(restTemplate.getForObject(anyString(), eq(FlightResponse.class))).thenReturn(flight);

        Optional<BookingResponse> result = service.createBooking(req);

        assertThat(result).isEmpty();
    }

    @Test
    void cancelBookingSuccess() {
    	
        when(bookingData.deleteById("a6b9479e")).thenReturn(true);
        
        assertThat(service.cancelBooking("a6b9479e")).isTrue();
        
    }

    @Test
    void cancelBookingNotFound() {
    	
        when(bookingData.deleteById("BOOK999")).thenReturn(false);
        
        assertThat(service.cancelBooking("BOOK999")).isFalse();
        
    }
}
