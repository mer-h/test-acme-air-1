package nz.co.acmeair.flight.service;

import java.util.List;
import java.util.Optional;

import nz.co.acmeair.flight.model.FlightRequest;
import nz.co.acmeair.flight.model.FlightResponse;

public interface FlightService {
	
	 Optional<List<FlightResponse>> searchAvailableFlights(FlightRequest flightReq);
	 
	 Optional<FlightResponse> searchFlightById(String flightId);
	
}
