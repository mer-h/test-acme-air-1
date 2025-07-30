package nz.co.acmeair.booking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import nz.co.acmeair.booking.data.BookingData;
import nz.co.acmeair.booking.model.BookingRequest;
import nz.co.acmeair.booking.model.BookingResponse;
import nz.co.acmeair.booking.service.BookingService;
import nz.co.acmeair.flight.model.FlightResponse;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the BookingService interface.
 * Handles business logic for creating and deleting the booking flights.
 */
@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final RestTemplate restTemplate;

    @Value("${flight.service.url}")
    private String flightServiceUrl; 
    
    private final BookingData bookingData;

    public BookingServiceImpl(BookingData bookingData, RestTemplate restTemplate) {
    	
        this.bookingData = bookingData;
        this.restTemplate = restTemplate;
        
    }


    /**
     * Creates a flight booking if the flight exists and has enough seats.
     *
     * @param request the booking request includes flight ID, passenger name, and number of seats
     * @return an Optional containing the BookingResponse if successful, or Optional.empty() if the flight
     *         does not exist or no enough seats
     */
    @Override
    public Optional<BookingResponse> createBooking(BookingRequest request) {
    	
    	//create a random string of letters and numbers and length of 8
    	String bookingId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    	
    	 FlightResponse flight;
    	 
         try {
        	 
        	 //call the flight api to search by flightId in all available flights
             flight = restTemplate.getForObject(
                     flightServiceUrl + request.flightId(),
                     FlightResponse.class
             );
             
         } catch (Exception e) {
 			logger.error("Unable to connect to Flight Api!");
             return Optional.empty();
         }

         if (flight == null || flight.getPassengers() < request.numberOfSeats()) {
             return Optional.empty();
         }

         double total = request.numberOfSeats() * flight.getPrice();

         BookingResponse booking = new BookingResponse(
                 bookingId,
                 flight.getFlightId(),
                 request.passengerName(),
                 request.numberOfSeats(),
                 total
         );

         bookingData.save(booking);

         return Optional.of(booking);        
    }
    
    /**
     * Delete a flight booking if the booking exists.
     *
     * @param request the bookingId
     * @return True if deleted successfully, or false if the booking does not exist
     */

	@Override
	public boolean cancelBooking(String bookingId) {
		
			return bookingData.deleteById(bookingId);
			
	}


    
}