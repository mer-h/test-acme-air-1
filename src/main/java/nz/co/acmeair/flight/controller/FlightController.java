package nz.co.acmeair.flight.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import jakarta.validation.Valid;
import nz.co.acmeair.flight.exception.FlightNotFoundException;
import nz.co.acmeair.flight.model.FlightRequest;
import nz.co.acmeair.flight.model.FlightResponse;
import nz.co.acmeair.flight.service.FlightService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for searching one-way available flights.
 */
@RestController
@RequestMapping("api/public/v1/flights")
@Tag(name = "Flight API", description = "search Flights")
public class FlightController {

	private final FlightService flightService;

	public FlightController(FlightService flightService) {
		this.flightService = flightService;
	}

	/**
	 * End-point to search for available one-way flights.
	 *
	 * @param valid flight request includes from, to, date and passengers
	 * @return 200 if found available flights, or 404 if flight not found
	 */

	@GetMapping("/search")
	public ResponseEntity<List<FlightResponse>> searchAvailableFlights(@Valid FlightRequest flightReq) {

		try {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate travelDate = LocalDate.parse(flightReq.date(), formatter);

			if (!travelDate.isAfter(LocalDate.now())) {
				throw new IllegalArgumentException("Travel date must be in the future.");
			}

			List<FlightResponse> flights = flightService.searchAvailableFlights(flightReq)
					.orElseThrow(() -> new FlightNotFoundException("No available flights found for given details."));

			return ResponseEntity.ok(flights);

		} catch (DateTimeParseException | IllegalArgumentException e) {
			throw new FlightNotFoundException("Invalid date format. Expected 'yyyy-MM-dd' and must be a future date.");
		}

	}

	/**
	 * End-point to search for flight by flight id.
	 *
	 * @param valid flight id
	 * @return 200 if found the flight by flight id, or 404 if flight not found
	 */

	@GetMapping("/{flightId}")
	public ResponseEntity<FlightResponse> getFlightById(@Valid @PathVariable String flightId) {

		FlightResponse flight = flightService.searchFlightById(flightId)
				.orElseThrow(() -> new FlightNotFoundException("Flight not found with ID: " + flightId));

		return ResponseEntity.ok(flight);
	}

}
