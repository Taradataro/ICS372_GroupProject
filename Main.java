package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Dealership> dealerships = new HashMap<>();
        dealerships.put("77338", new Dealership("77338", true));
        dealerships.put("12513", new Dealership("12513", true));

        // 6. Read and process vehicle inventory from JSON files
        String[] jsonFiles = {"testFile.json", "testFile1.json"};
        for (String file : jsonFiles) {
            readAndAddVehiclesFromJson(Paths.get("src", "main", "java", "org", "example", file).toString(), dealerships);
        }

        // 7. Disable dealerships from receiving new vehicles
        dealerships.get("77338").disableReceiving();
        dealerships.get("12513").disableReceiving();

        // 8. Export dealership data to JSON files
        jsonExport.exportDealershipToJson(dealerships.get("77338"), "userInput.json");
        jsonExport.exportDealershipToJson(dealerships.get("12513"), "userInput1.json");

        // 9. Display current vehicle inventory for each dealership
        dealerships.values().forEach(Dealership::printCurrentVehicles);
    }

    private static void readAndAddVehiclesFromJson(String filePath, Map<String, Dealership> dealerships) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONArray carInventory = (JSONArray) jsonObject.get("car_inventory");

            for (Object obj : carInventory) {
                JSONObject vehicleJson = (JSONObject) obj;
                String dealershipId = (String) vehicleJson.get("dealership_id");
                Dealership dealership = dealerships.get(dealershipId);
                if (dealership != null && dealership.isEnabled()) {
                    String vehicleId = (String) vehicleJson.get("vehicle_id");
                    String manufacturer = (String) vehicleJson.get("vehicle_manufacturer");
                    String model = (String) vehicleJson.get("vehicle_model");
                    long price = (long) vehicleJson.get("price");
                    LocalDate acquisitionDate = convertTimestampToDate((long) vehicleJson.get("acquisition_date"));
                    String vehicleType = (String) vehicleJson.get("vehicle_type");

                    Vehicle vehicle = new Vehicle(vehicleId, manufacturer, model, acquisitionDate, price, vehicleType);
                    dealership.addVehicle(vehicle);
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }

    private static LocalDate convertTimestampToDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
