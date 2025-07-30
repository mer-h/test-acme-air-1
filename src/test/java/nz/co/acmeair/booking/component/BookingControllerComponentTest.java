// File: src/test/java/nz/co/acmeair/booking/component/BookingControllerComponentTest.java
package nz.co.acmeair.booking.component;

import nz.co.acmeair.TestAcmeAir1Application;
import nz.co.acmeair.booking.model.BookingRequest;
import nz.co.acmeair.booking.model.BookingResponse;
import nz.co.acmeair.flight.model.FlightResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = TestAcmeAir1Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookingControllerComponentTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private RestTemplate flightRestTemplate;

    private String baseUrl() {
    	
        return "http://localhost:" + port + "/api/public/v1/bookings";
        
    }

    @Test
    void createBookingSuccess() {
    	
        BookingRequest request = new BookingRequest("A4075D0814", "Mercedeh", 2);
        FlightResponse flight = new FlightResponse("A4075D0814", "A4075", "Auckland", "Bali", "2025-08-14", 10, 860);

        when(flightRestTemplate.getForObject(contains("A4075"), eq(FlightResponse.class))).thenReturn(flight);

        ResponseEntity<BookingResponse> response = restTemplate.postForEntity(baseUrl(), request, BookingResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().passengerName()).isEqualTo("Mercedeh");
        assertThat(response.getBody().flightId()).isEqualTo("A4075D0814");
    }

    @Test
    void createBookingWithInvalidFlight() {
    	
        BookingRequest req = new BookingRequest("INVALID123", "Mercedeh", 2);
        when(flightRestTemplate.getForObject(anyString(), eq(FlightResponse.class))).thenThrow(new RuntimeException());

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl(), req, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Unable to create booking");
    }

    
    @Test
    void deleteBookingSuccess() {
    	
        String bookingId = "ABC12345";


        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl() + "/" + bookingId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isIn(HttpStatus.NO_CONTENT, HttpStatus.NOT_FOUND);
        
    }

    @Test
    void deleteBookingNotFound() {
    	
        String bookingId = "qq23llop";

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/" + bookingId,
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Booking not found");
        
    }
}
