package nz.co.acmeair.flight.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import nz.co.acmeair.flight.model.FlightResponse;

@Component
public class FlightDataProvider {
	
    private final Map<String, FlightResponse> flightDetails = new HashMap<>();
	 
	
	public FlightDataProvider() {
		initData();
	}

	private void initData() {
		
		flightDetails.put("A4075D0814", new FlightResponse("A4075D0814", "A4075", "Auckland", "Bali", "2025-08-14", 11, 860));
		flightDetails.put("A4090D0824", new FlightResponse("A4090D0824", "A4090", "Auckland", "Sydney", "2025-08-24", 8, 530));
		flightDetails.put("A5060D0901", new FlightResponse("A5060D0901","A5060", "Auckland", "Wellington", "2025-09-01", 15, 230));
		
    }
	
	public Map<String, FlightResponse> getFlightDetails(){
		return flightDetails;
	}
	
	public List<FlightResponse> getAllFlights() {
        return new ArrayList<>(flightDetails.values());
    }

    public Optional<FlightResponse> getFlightById(String flightId) {
        return Optional.ofNullable(flightDetails.get(flightId));
    }

}
