package org.example.hellofx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReadXMLFileTest {

    @Test
    void getDealers(@TempDir Path tempDir) throws Exception {
        // XML with one dealer and one vehicle
        String xml = """
            <Dealers>
                <Dealer id=\"ID1\">
                    <Name>Name1</Name>
                    <Vehicle type=\"TypeX\" id=\"V1\">
                        <Price unit=\"dollars\">100</Price>
                        <Make>MakeX</Make>
                        <Model>ModelX</Model>
                    </Vehicle>
                </Dealer>
            </Dealers>
        """;

        // Temp file
        Path file = tempDir.resolve("dealers.xml");
        Files.writeString(file, xml);

        // Execute
        ReadXMLFile reader = new ReadXMLFile();
        List<Dealer> dealers = reader.getDealers(file.toString());

        // Assertions
        assertNotNull(dealers, "Expected non-null dealer list");
        assertEquals(1, dealers.size(), "Should read exactly one dealer");

        Dealer d = dealers.get(0);
        assertEquals("ID1", d.getId(), "Dealer ID should match");
        assertEquals("Name1", d.getName(), "Dealer name should match");

        List<Vehicle> vehicles = d.getVehicles();
        assertNotNull(vehicles, "Expected non-null vehicle list");
        assertEquals(1, vehicles.size(), "Dealer should have one vehicle");

        Vehicle v = vehicles.get(0);
        assertEquals("V1", v.getId(), "Vehicle ID should match");
        assertEquals("TypeX", v.getType(), "Vehicle type should match");
        assertEquals("MakeX", v.getMake(), "Vehicle make should match");
        assertEquals("ModelX", v.getModel(), "Vehicle model should match");
        // ReadXMLFile prefixes $ and space for dollars
        assertEquals("$ 100", v.getPrice(), "Vehicle price should include dollar symbol and value");
        assertEquals("Available", v.getStatus(), "Default vehicle status should be Available");
    }
}
