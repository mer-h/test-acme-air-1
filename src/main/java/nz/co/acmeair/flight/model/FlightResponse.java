package nz.co.acmeair.flight.model;

import java.util.Objects;

public class FlightResponse {
	
    private String flightId;
    
    private String flightNumber;

    private String from;
    
    private String to;
    
    private String date;
    
    private int passengers;
    
    private double price;
    
    
    public FlightResponse(String flightId,String flightNumber, String from, String to, 
    		String date, int passengers, double price) {
    	
    	this.flightId = flightId;
    	this.flightNumber = flightNumber;
    	this.from = from;
    	this.to = to;
    	this.date = date;
    	this.passengers = passengers;
    	this.price = price;

		
	}
    
    public String getDate() {
		return date;
	}
    
    public String getFlightId() {
		return flightId;
	}
    
    public String getFlightNumber() {
		return flightNumber;
	}
    
    public String getFrom() {
		return from;
	}
    
    public int getPassengers() {
		return passengers;
	}
    
     public double getPrice() {
		return price;
	}
     
    public String getTo() {
		return to;
	}
    
    public void setDate(String date) {
		this.date = date;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setPassengers(int passengers) {
		this.passengers = passengers;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(date, flightId, from, passengers, price, to);
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlightResponse other = (FlightResponse) obj;
		
		return  Objects.equals(flightId, other.flightId)
				&& Objects.equals(flightNumber, other.flightNumber) 
				&& Objects.equals(from, other.from) 
				&& Objects.equals(to, other.to);
	}

	@Override
	public String toString() {
		return "FlightResponse [flightId=" + flightId + ", flightNumber=" + flightNumber + ", from=" + from + ", to="
				+ to + ", date=" + date + ", passengers=" + passengers + ", price=" + price + "]";
	}


}
