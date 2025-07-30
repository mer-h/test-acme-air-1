package nz.co.acmeair.booking.controller;

import jakarta.validation.Valid;
import nz.co.acmeair.booking.model.BookingRequest;
import nz.co.acmeair.booking.model.BookingResponse;
import nz.co.acmeair.booking.service.BookingService;
import nz.co.acmeair.flight.exception.BookingNotFoundException;
import nz.co.acmeair.flight.exception.FlightNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * REST controller for managing flight bookings.
 */
@RestController
@RequestMapping("/api/public/v1/bookings")
@Tag(name = "Booking Flight API", description = "Create and save a passenger’s flight booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
    	
        this.bookingService = bookingService;
        
    }

    
    /**
     * End-point to create a new flight booking.
     *
     * @param valid booking request includes flightId, passengerName, and numberOfSeats
     * @return 200 if the booking is successful, or 404 if flight not found or seats are not enough
     */
    @Operation(summary = "Create and save a passenger’s flight booking",
            description = "First get the booking details: flightId, passengerName, numberOfSeats. Then check if the "
            		+ " flight number is exist will save the details")
	 @ApiResponses(value = {
		 @ApiResponse(responseCode = "400", description = "Bad request - Make sure you entered correct format of data."),
	     @ApiResponse(responseCode = "200", description = "Passenger’s flight booking is saved "),
	     @ApiResponse(responseCode = "404", description = "Flight id is not valid or not enough seats")
	 })

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
    	
        BookingResponse response = bookingService.createBooking(request)
            .orElseThrow(() -> new FlightNotFoundException("Unable to create booking. Flight not found or not enough seats."));
        
        return ResponseEntity.ok(response);
        
    }
    
    
    /**
     * End-point to delete a flight booking.
     *
     * @param  booking id
     * @return 204 if the booking is found and deleted, or 404 if the booking not found
     */
    @Operation(summary = "Cancel an existing passenger’s flight booking by booking id",
            description = "First get the booking id, then check if the "
            		+ " booking exists in the momory list then delete the record")
	 @ApiResponses(value = {
		 @ApiResponse(responseCode = "400", description = "Bad request - Make sure you entered correct format of booking id(letters and numbers)."),
	     @ApiResponse(responseCode = "204", description = "Passenger’s flight booking is found and deleted"),
	     @ApiResponse(responseCode = "404", description = "Bookinbg id is not valid or not found the matching booking id")
	 })
    
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> cancelBookingById(@PathVariable String bookingId) {
    	
    	 // Validation: only letters and numbers
        if (bookingId.isBlank() || bookingId.stripLeading().length() <8 || !bookingId.matches("^[a-zA-Z0-9]+$")) {
            throw new BookingNotFoundException("Invalid booking ID format. BookingId can only contais letters and numbers!");
        }
    	
        boolean deleted = bookingService.cancelBooking(bookingId);
        
        if (!deleted) {
            throw new BookingNotFoundException("Booking not found: " + bookingId);
        }

        return ResponseEntity.noContent().build();
        
    }
}