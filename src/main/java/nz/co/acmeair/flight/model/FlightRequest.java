package nz.co.acmeair.flight.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record FlightRequest
		(
		        @NotBlank String from,
		        
		        @NotBlank String to,
		        
		        @NotBlank String date,
		        
		        @Min(1) int passengers
		) 
{

}
