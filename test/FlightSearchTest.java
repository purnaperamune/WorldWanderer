import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FlightSearchTest {

    private final FlightSearch search = new FlightSearch();

    // Happy path: All input parameters are valid, returning true.
    @Test
    void testValidFlightSearch() {
        assertTrue(search.runFlightSearch(
                "25/12/2025", "syd", false, "05/01/2026", "lax",
                "economy", 2, 1, 0));
    }

    // Condition 1: Total passengers count should be between 1 and 9.
    @Test
    void testTooManyPassengers() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", false, "05/01/2026", "lax",
                "economy", 10, 0, 0));
    }

    @Test
    void testNoPassengers() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", false, "05/01/2026", "lax",
                "economy", 0, 0, 0));
    }

    // Condition 2: Children restrictions.
    // Children cannot be seated in emergency row seating OR first class.
    @Test
    void testChildrenInFirstClass() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", false, "05/01/2026", "lax",
                "first", 1, 1, 0));
    }

    @Test
    void testChildrenInEmergencyRow() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", true, "05/01/2026", "lax",
                "economy", 1, 1, 0));
    }

    // Condition 3: Infants restrictions.
    // Infants cannot be seated in emergency row seating OR business class.
    @Test
    void testInfantsInBusinessClass() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", false, "05/01/2026", "lax",
                "business", 1, 0, 1));
    }

    @Test
    void testInfantsInEmergencyRow() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", true, "05/01/2026", "lax",
                "economy", 1, 0, 1));
    }

    // Condition 4: Children adjacency (≤ 2 children per adult).
    @Test
    void testTooManyChildrenPerAdult() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", false, "05/01/2026", "lax",
                "economy", 1, 3, 0));
    }

    // Condition 5: Infants on lap (≤ 1 infant per adult).
    @Test
    void testTooManyInfantsPerAdult() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", false, "05/01/2026", "lax",
                "economy", 1, 0, 2));
    }

    // Condition 6: Departure not in the past.
    @Test
    void testDepartureInPast() {
        assertFalse(search.runFlightSearch(
                "01/01/2020", "syd", false, "05/01/2020", "lax",
                "economy", 2, 0, 0));
    }

    // Condition 7: Invalid date format
    // Valid format: "05/01/2026", but here the code checks for "2026/01/05", which is an invalid date format.
    @Test
    void testInvalidDateFormat() {
        assertFalse(search.runFlightSearch(
                "2025/12/25", "syd", false, "2026/01/05", "lax",
                "economy", 2, 0, 0));
    }

    // Condition 8: Return must be after departure (not equal/before).
    @Test
    void testReturnBeforeDeparture() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", false, "20/12/2025", "lax",
                "economy", 2, 0, 0));
    }

    // Condition 9: Invalid seating class
    // Allowed seating classes are only "economy", "premium economy", "business", and "first".
    @Test
    void testInvalidSeatingClass() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", false, "05/01/2026", "lax",
                "vip", 2, 0, 0));
    }

    // Condition 10: Emergency row restriction.
    @Test
    void testEmergencyRowNotEconomy() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", true, "05/01/2026", "lax",
                "business", 2, 0, 0));
    }

    // Condition 11: Invalid airport codes.
    // Allowed airport codes: "syd", "mel", "lax", "cdg", "del", "pvg", "doh".
    @Test
    void testInvalidAirportCodes() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "abc", false, "05/01/2026", "xyz",
                "economy", 2, 0, 0));
    }

    // Condition 11: Airport codes for departure and destination cannot be the same.
    @Test
    void testSameAirportCodes() {
        assertFalse(search.runFlightSearch(
                "25/12/2025", "syd", false, "05/01/2026", "syd",
                "economy", 2, 0, 0));
    }
}