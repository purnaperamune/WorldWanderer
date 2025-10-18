public class Main {
    public static void main(String[] args) {
        FlightSearch search = new FlightSearch();

        boolean result = search.runFlightSearch(
                "25/12/2025",   // departureDate
                "syd",          // departureAirportCode
                false,          // emergencyRowSeating
                "05/01/2026",   // returnDate
                "lax",          // destinationAirportCode
                "economy",      // seatingClass
                2,              // adultPassengerCount
                1,              // childPassengerCount
                0               // infantPassengerCount
        );

        if (result) {
            System.out.println("Flight search successful!\n" +
                    "Departure: " + search.getDepartureAirportCode() + " on " + search.getDepartureDate() + "\n" + "Destination: " + search.getDestinationAirportCode() + " | Return: " + search.getReturnDate() + "\n" + "Class: " + search.getSeatingClass() + " | Emergency Row: " + search.isEmergencyRowSeating() + "\n" +
                    "Passengers - Adults: " + search.getAdultPassengerCount() +
                    ", Children: " + search.getChildPassengerCount() +
                    ", Infants: " + search.getInfantPassengerCount());
        } else {
            System.out.println("Flight search failed as one or more conditions were invalid.");
        }
    }
}