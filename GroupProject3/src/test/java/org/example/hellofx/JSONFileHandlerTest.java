package org.example.hellofx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JSONFileHandlerTest {

    @Test
    void getDealers(@TempDir Path tempDir) throws Exception {
        // Prepare a JSON file with one dealer and one vehicle
        String json = """
        {
          "Dealers": [
            {
              "Dealer": {
                "id": "D1",
                "name": "DealerOne",
                "vehicles": [
                  {
                    "id": "V1",
                    "type": "SUV",
                    "make": "MakeA",
                    "model": "ModelA",
                    "price": "10000",
                    "status": "Available"
                  }
                ]
              }
            }
          ]
        }
        """;
        Path file = tempDir.resolve("dealers.json");
        Files.writeString(file, json);

        // getDealers()
        JSONFileHandler handler = new JSONFileHandler();
        List<Dealer> dealers = handler.getDealers(file.toString());

        // Verify
        assertNotNull(dealers, "getDealers should not return null");
        assertEquals(1, dealers.size(), "Should read one dealer");
        Dealer d = dealers.get(0);
        assertEquals("D1", d.getId());
        assertEquals("DealerOne", d.getName());
        assertNotNull(d.getVehicles());
        assertEquals(1, d.getVehicles().size(), "Dealer should have one vehicle");

        Vehicle v = d.getVehicles().get(0);
        assertEquals("V1", v.getId());
        assertEquals("SUV", v.getType());
        assertEquals("MakeA", v.getMake());
        assertEquals("ModelA", v.getModel());
        assertEquals("10000", v.getPrice());
        assertEquals("Available", v.getStatus());
    }

    @Test
    void saveAsJson(@TempDir Path tempDir) throws Exception {
        // Create a dealer with one vehicle
        Dealer d = new Dealer("D2", "DealerTwo");
        Vehicle v = new Vehicle("Sedan", "V2", "20000", "MakeB", "ModelB");
        v.setStatus("Rented");
        d.setVehicles(Collections.singletonList(v));

        Path file = tempDir.resolve("out.json");
        JSONFileHandler.saveAsJson(Collections.singletonList(d), file.toString());

        assertTrue(Files.exists(file), "Output JSON file should exist");

        // Read back the file and verify contents via getDealers()
        JSONFileHandler handler = new JSONFileHandler();
        List<Dealer> dealers = handler.getDealers(file.toString());

        assertNotNull(dealers);
        assertEquals(1, dealers.size());
        Dealer d2 = dealers.get(0);
        assertEquals("D2", d2.getId());
        assertEquals("DealerTwo", d2.getName());
        assertNotNull(d2.getVehicles());
        assertEquals(1, d2.getVehicles().size());

        Vehicle v2 = d2.getVehicles().get(0);
        assertEquals("V2", v2.getId());
        assertEquals("Sedan", v2.getType());
        assertEquals("MakeB", v2.getMake());
        assertEquals("ModelB", v2.getModel());
        assertEquals("20000", v2.getPrice());
        assertEquals("Rented", v2.getStatus());
    }
}