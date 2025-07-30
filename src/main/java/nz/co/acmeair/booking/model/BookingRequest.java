package nz.co.acmeair.booking.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookingRequest(
		
    @NotBlank String flightId,
        
    @NotBlank String passengerName,
    
    @Min(1) int numberOfSeats
    
) 
{}