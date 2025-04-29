import org.example.hellofx.Dealer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.example.hellofx.Vehicle

class VehicleTest {

    // Test the default constructor
    @Test
    fun testDefaultConstructor() {
        val vehicle = Vehicle()

        // Checking default values for the properties
        assertEquals("", vehicle.getId(), "Id should be an empty string by default")
        assertEquals("", vehicle.getType(), "Type should be an empty string by default")
        assertEquals("", vehicle.getMake(), "Make should be an empty string by default")
        assertEquals("", vehicle.getModel(), "Model should be an empty string by default")
        assertEquals("", vehicle.getPrice(), "Price should be an empty string by default")
        assertEquals("Available", vehicle.getStatus(), "Status should be 'Available' by default")
    }

    // Test the setters and getters for status
    @Test
    fun testStatusSetterAndGetter() {
        val vehicle = Vehicle("SUV", "1234", "30000", "Toyota", "Highlander")

        // Set status to 'Rented'
        vehicle.setStatus("Rented")
        assertEquals("Rented", vehicle.getStatus(), "Status should be 'Rented' after calling setStatus('Rented')")

        // Set status back to 'Available'
        vehicle.setStatus("Available")
        assertEquals("Available", vehicle.getStatus(), "Status should be 'Available' after calling setStatus('Available')")
    }

    @Test
    fun testAddVehicle() {
        // Create a dealer
        val dealer = Dealer("1", "Wacky Bob’s Automall")

        // Create a vehicle
        val vehicle = Vehicle("SUV", "000", "30000", "Toyota", "Highlander")

        // Add vehicle to dealer
        dealer.addVehicle(vehicle)

        // Assert that the vehicle is added to the dealer's vehicle list
        assertTrue(dealer.getVehicles().contains(vehicle), "The vehicle should be added to the dealer's vehicle list")
    }
}
