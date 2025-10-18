import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;

public class FlightSearch {

    // Attributes
    private String departureDate;
    private String departureAirportCode;
    private boolean emergencyRowSeating;
    private String returnDate;
    private String destinationAirportCode;
    private String seatingClass;
    private int adultPassengerCount;
    private int childPassengerCount;
    private int infantPassengerCount;

    // Airports that are available for search.
    private static final List<String> ALLOWED_AIRPORTS =
            List.of("syd", "mel", "lax", "cdg", "del", "pvg", "doh");

    // Seating classes that are available for search.
    private static final List<String> ALLOWED_CLASSES =
            List.of("economy", "premium economy", "business", "first");

    private static final DateTimeFormatter STRICT_DDMMYYYY =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Validating all 11 conditions. This method initializes attributes and returns true,
     * if and only if all these conditions pass. Else, returns false.
     */
    public boolean runFlightSearch(String departureDate, String departureAirportCode, boolean emergencyRowSeating, String returnDate, String destinationAirportCode, String seatingClass, int adultPassengerCount, int childPassengerCount, int infantPassengerCount) {

        // Normalizing string inputs to lowercase as per specification.
        String depDate = returnLowerCase(departureDate);
        String retDate = returnLowerCase(returnDate);
        String depCode = returnLowerCase(departureAirportCode);
        String dstCode = returnLowerCase(destinationAirportCode);
        String seatClass = returnLowerCase(seatingClass);

        // Condition 1: Child/infant counts cannot be negative as in the announcement.
        if (childPassengerCount < 0 || infantPassengerCount < 0) {
            return false;
        }

        // Condition 1: Total passenger count validation.
        int passengerCount = adultPassengerCount + childPassengerCount + infantPassengerCount;
        if (passengerCount < 1 || passengerCount > 9) {
            return false;
        }

        // Condition 9: Seating class validation.
        if (!ALLOWED_CLASSES.contains(seatClass)) {
            return false;
        }

        // Condition 11: Airport codes validation (and cannot be the same).
        if (!ALLOWED_AIRPORTS.contains(depCode) || !ALLOWED_AIRPORTS.contains(dstCode)) {
            return false;
        }
        if (depCode.equals(dstCode)) {
            return false;
        }

        // Conditions 6,7 and 8: Date validity & ordering.
        LocalDate dep;
        LocalDate ret;
        try {
            dep = LocalDate.parse(depDate, STRICT_DDMMYYYY);   // C7: strict format & valid date
            ret = LocalDate.parse(retDate, STRICT_DDMMYYYY);   // C7: strict format & valid date
        } catch (Exception e) {
            return false;
        }

        // Condition 6: Departure cannot be in the past (Using today as reference).
        if (dep.isBefore(LocalDate.now())) {
            return false;
        }

        // Condition 8: Two-way only, return must be AFTER departure (not equal/before).
        if (!ret.isAfter(dep)) {
            return false;
        }

        // Condition 10 (clarified wording): Only *economy seating* can have an emergency row.
        if (emergencyRowSeating && !seatClass.equals("economy")) {
            return false;
        }

        // Condition 2: Children restrictions (no children in emergency row OR first class).
        if ((emergencyRowSeating && childPassengerCount > 0) ||
                (seatClass.equals("first") && childPassengerCount > 0)) {
            return false;
        }

        // Condition 3: Infants restrictions (no infants in emergency row OR business class).
        if ((emergencyRowSeating && infantPassengerCount > 0) ||
                (seatClass.equals("business") && infantPassengerCount > 0)) {
            return false;
        }

        // Condition 4: Children adjacency (≤ 2 children per adult).
        if (childPassengerCount > adultPassengerCount * 2) {
            return false;
        }

        // Condition 5: Infants on lap (≤ 1 infant per adult).
        if (infantPassengerCount > adultPassengerCount) {
            return false;
        }

        // All validations passed → initialize attributes (Note 7: only here) and return true.
        this.departureDate = depDate;
        this.departureAirportCode = depCode;
        this.emergencyRowSeating = emergencyRowSeating;
        this.returnDate = retDate;
        this.destinationAirportCode = dstCode;
        this.seatingClass = seatClass;
        this.adultPassengerCount = adultPassengerCount;
        this.childPassengerCount = childPassengerCount;
        this.infantPassengerCount = infantPassengerCount;

        return true;
    }

    // Helper function to convert parameters to lower case.
    private static String returnLowerCase(String s) {
        return (s == null) ? "" : s.toLowerCase().trim();
    }

    // Getters for tests.
    public String getDepartureDate() { return departureDate; }
    public String getReturnDate() { return returnDate; }
    public String getDepartureAirportCode() { return departureAirportCode; }
    public String getDestinationAirportCode() { return destinationAirportCode; }
    public String getSeatingClass() { return seatingClass; }
    public boolean isEmergencyRowSeating() { return emergencyRowSeating; }
    public int getAdultPassengerCount() { return adultPassengerCount; }
    public int getChildPassengerCount() { return childPassengerCount; }
    public int getInfantPassengerCount() { return infantPassengerCount; }
}