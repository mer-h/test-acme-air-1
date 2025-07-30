package nz.co.acmeair.booking.model;

public record BookingResponse(
		
	    String bookingId,
	    
	    String flightId,
	    
	    String passengerName,
	    
	    int numberOfSeats,
	    
	    double totalPrice
	    
	) 
{}
