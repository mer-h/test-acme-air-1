package nz.co.acmeair.flight.component;

import nz.co.acmeair.TestAcmeAir1Application;
import nz.co.acmeair.flight.data.FlightDataProvider;
import nz.co.acmeair.flight.model.FlightResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestAcmeAir1Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightControllerComponentTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private FlightDataProvider mockProvider;

    private String baseUrl() {
    	
        return "http://localhost:" + port + "/api/public/v1/flights";
        
    }

    @Test
    void searchFlightSuccess() {
    	
    	//FlightResponse(String flightId,String from, String to, String date, int passengers, double price  )
        FlightResponse flight = new FlightResponse("A4075D0814","A4075D0814", "Auckland", "Bali", "2025-08-14", 10, 860);
        when(mockProvider.getAllFlights()).thenReturn(List.of(flight));

        String url = baseUrl() + "/search?from=Auckland&to=Bali&date=2025-08-14&passengers=1";
        ResponseEntity<FlightResponse[]> response = restTemplate.getForEntity(url, FlightResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getFlightId()).isEqualTo("A4075D0814");
        
    }

    @Test
    void searchFlightNotFound() {
    	
        when(mockProvider.getAllFlights()).thenReturn(List.of());

        String url = baseUrl() + "/search?from=Auckland&to=Bali&date=2025-08-14&passengers=1";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("No available flights");
        
    }

    @Test
    void getFlightByIdSuccess() {
    	
        FlightResponse flight = new FlightResponse("A4075D0814","A4075D0814", "Auckland", "Bali", "2025-08-14", 10, 860);
        when(mockProvider.getFlightById("A4075D0814")).thenReturn(Optional.of(flight));
        

        String url = baseUrl() + "/A4075D0814";
        ResponseEntity<FlightResponse> response = restTemplate.getForEntity(url, FlightResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFlightId()).isEqualTo("A4075D0814");
        
    }

    @Test
    void getFlightByIdNotFound() {
    	
        when(mockProvider.getFlightById("QQ9988")).thenReturn(Optional.empty());

        String url = baseUrl() + "/QQ9988";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Flight not found");
        
    }
    
}