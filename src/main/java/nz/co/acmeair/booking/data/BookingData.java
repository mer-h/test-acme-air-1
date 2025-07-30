package nz.co.acmeair.booking.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import nz.co.acmeair.booking.model.BookingResponse;

/**
 * In-memory booking store using ConcurrentHashMap.
 */
@Component
public class BookingData {
	
    private final Map<String, BookingResponse> bookings = new ConcurrentHashMap<>();

    public void save(BookingResponse booking) {
    	
        bookings.put(booking.bookingId(), booking);
    }

    public Optional<BookingResponse> getById(String bookingId) {
    	
        return Optional.ofNullable(bookings.get(bookingId));
    }

    public List<BookingResponse> getAll() {
    	
        return new ArrayList<>(bookings.values());
    }
    
    public boolean deleteById(String bookingId) {
    	
    	if(getById(bookingId).isPresent()) {
    		bookings.remove(bookingId);
    		return true;
    	}
        return false;
    }

    
}