package nz.co.acmeair.flight.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nz.co.acmeair.flight.data.FlightDataProvider;
import nz.co.acmeair.flight.model.FlightRequest;
import nz.co.acmeair.flight.model.FlightResponse;
import nz.co.acmeair.flight.service.FlightService;

/**
 * Implementation of the FlightService interface.
 * Handles business logic for searching available flights.
 */

@Service
public class FlightServiceImpl implements FlightService {

	private FlightDataProvider dataProvider;

	public FlightServiceImpl(FlightDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

    /**
     * Search available flights with given FlightRequest
     *
     * @param request the flight request includes from, to, date, and passengers
     * @return an Optional containing the list of FlightResponses if successful, or Optional.empty() if found no available flights
     *         
     */
	@Override
	public Optional<List<FlightResponse>> searchAvailableFlights(FlightRequest req) {

		List<FlightResponse> availableFlights = dataProvider.getAllFlights().stream()
				.filter(f ->   f.getFrom().equalsIgnoreCase(req.from()) 
							&& f.getTo().equalsIgnoreCase(req.to())
							&& f.getDate().equals(req.date()) 
							&& f.getPassengers() >= req.passengers())
				.collect(Collectors.toList());

		return availableFlights.isEmpty() ? Optional.empty() : Optional.of(availableFlights);
		
	}

    /**
     * Search flights by flight id
     *
     * @param request the flight id
     * @return an Optional containing the  FlightResponse if successful, or Optional.empty() if  no available flight found by the id
     *         
     */
	@Override
	public Optional<FlightResponse> searchFlightById(String flightId) {
		
		return dataProvider.getFlightById(flightId);
		
	}
	
}
