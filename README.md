
# Acme Air - Flight Search & Booking API

A simple Spring Boot 3 REST API to search flights and booking tickets using in-memory data.

## Technologies
```
- Java 21
- Gradle / Maven
- Spring Boot 3
- SLF4J Logging
- Swagger / OpenAPI
- Mockito/JUnit for Testing
```

## How to Run with Gradle
```
./gradlew test && ./gradlew bootRun
```

## Swagger / API Documentation
```
http://localhost:8080/swagger-ui/index.html
```

## API Endpoints

### Search Flights

```
curl --location 'http://localhost:8080/api/public/v1/flights/search?from=Auckland&to=Bali&date=2025-08-14&passengers=2'
```

### Get Flight by ID

```
curl --location 'http://localhost:8080/api/public/v1/flights/A4075D0814'
```

### Create Booking

```
curl --location 'http://localhost:8080/api/public/v1/bookings'
--header 'Content-Type: application/json' \
--data '{
    "flightId":"A4075D0814",
    "passengerName":"Mercedeh",
    "numberOfSeats":2
}'
```

### Delete a Booking

```
curl --location --request DELETE 'http://localhost:8080/api/public/v1/bookings/c62cefd1'
```

## Notes
* Current version is designed just for one-way flights
* All data is stored in memory (no database)
* Returns 400 for bad request/ 404 if no flight or booking found / 200 for successful booking or searching flights / 204 for successful delete

## TODO
* Create and save a passenger’s flight booking is implemented based on assumption that we always have available seats, but it needs to address accordingly in the next version. It needs to be transactional, so be able to save the booking and decrease seat numbers in the same time.

* Update passenger details on an existing booking

