package org.example.dealershipapplication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;


public class jsonExport {


    public static void exportAllDealers(DealershipManager manager, String filePath) {
        // Root object
        JSONObject root = new JSONObject();

        // Array of dealer objects
        JSONArray dealersArray = new JSONArray();

        // Iterate over each Dealership in the manager
        for (Dealership d : manager.getAllDealerships()) {
            JSONObject dealerJson = new JSONObject();
            dealerJson.put("id", d.getId());
            dealerJson.put("name", d.getName());
            dealerJson.put("receivingEnabled", d.isReceivingEnabled());

            // Vehicles array
            JSONArray vehiclesArray = new JSONArray();
            for (Vehicle v : d.getVehicles()) {
                JSONObject vJson = new JSONObject();
                vJson.put("id", v.getId());
                vJson.put("make", v.getMake());
                vJson.put("model", v.getModel());
                vJson.put("price", v.getPrice());
                vJson.put("type", v.getType());
                vJson.put("loaned", v.isLoaned());

                long epochMillis = v.getAcquisitionDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toEpochSecond() * 1000L;
                vJson.put("acquisitionDate", epochMillis);

                vehiclesArray.add(vJson);
            }
            dealerJson.put("vehicles", vehiclesArray);

            dealersArray.add(dealerJson);
        }

        // Place dealers array into the root object
        root.put("dealerships", dealersArray);

        // Write to file
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(root.toJSONString());
            System.out.println("Dealership data saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save dealership data: " + e.getMessage());
        }
    }

    /**
     * Loads all dealerships from the specified JSON file into a new DealershipManager.
     * @param filePath  Source file (e.g., "dealershipsData.json")
     * @return          A DealershipManager containing all loaded dealerships
     */
    public static DealershipManager loadAllDealers(String filePath) {
        DealershipManager manager = new DealershipManager();

        try (FileReader reader = new FileReader(filePath)) {
            JSONObject root = (JSONObject) new JSONParser().parse(reader);
            JSONArray dealersArray = (JSONArray) root.get("dealerships");
            if (dealersArray != null) {
                for (Object obj : dealersArray) {
                    JSONObject dealerJson = (JSONObject) obj;
                    String id = (String) dealerJson.get("id");
                    String name = (String) dealerJson.get("name");
                    boolean receivingEnabled = (boolean) dealerJson.get("receivingEnabled");

                    Dealership dealership = new Dealership(id, receivingEnabled);
                    dealership.setDealerName(name);

                    // Load vehicles
                    JSONArray vehiclesArray = (JSONArray) dealerJson.get("vehicles");
                    if (vehiclesArray != null) {
                        for (Object vObj : vehiclesArray) {
                            JSONObject vJson = (JSONObject) vObj;
                            Vehicle v = parseVehicle(vJson);
                            dealership.addVehicle(v);
                        }
                    }

                    manager.addDealership(dealership);
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Failed to load dealership data: " + e.getMessage());
            // Return an empty manager if there's an error
        }
        return manager;
    }

    /**
     * Helper method to parse a single Vehicle from a JSONObject (org.json.simple).
     */
    private static Vehicle parseVehicle(JSONObject vJson) {
        String id = (String) vJson.get("id");
        String make = (String) vJson.get("make");
        String model = (String) vJson.get("model");
        double price = ((Number) vJson.get("price")).doubleValue();
        String type = (String) vJson.get("type");
        boolean loaned = (boolean) vJson.get("loaned");

        long epochMillis = (long) vJson.get("acquisitionDate");
        LocalDate acquisitionDate = Instant.ofEpochMilli(epochMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Vehicle vehicle = new Vehicle(id, make, model, acquisitionDate, price, type);
        vehicle.setLoaned(loaned);
        return vehicle;
    }
}
