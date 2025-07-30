package nz.co.acmeair.booking.service;

import java.util.Optional;

import nz.co.acmeair.booking.model.BookingRequest;
import nz.co.acmeair.booking.model.BookingResponse;

public interface BookingService {
	
    Optional<BookingResponse> createBooking(BookingRequest request);
    
    boolean cancelBooking(String bookingId);

    
}