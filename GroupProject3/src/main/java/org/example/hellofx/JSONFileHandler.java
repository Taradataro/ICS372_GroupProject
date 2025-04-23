package org.example.hellofx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSONFileHandler implements FileHandler {
    public List<Dealer> getDealers(String inputPath) {
        List<Dealer> dealers = new ArrayList<>();

        try {
            // Read the entire file content as a String
            String content = new String(Files.readAllBytes(Paths.get(inputPath)));

            // Parse the root JSON object
            JSONObject root = new JSONObject(content);

            // Get the "Dealers" array
            JSONArray dealersArray = root.getJSONArray("Dealers");

            // Loop through each wrapped dealer
            for (int i = 0; i < dealersArray.length(); i++) {
                JSONObject wrappedDealer = dealersArray.getJSONObject(i);
                JSONObject dealerJson = wrappedDealer.getJSONObject("Dealer");

                String id = dealerJson.getString("id");
                String name = dealerJson.getString("name");

                Dealer dealer = new Dealer(id, name);

                // Parse vehicles
                JSONArray vehiclesArray = dealerJson.getJSONArray("vehicles");
                for (int j = 0; j < vehiclesArray.length(); j++) {
                    JSONObject vJson = vehiclesArray.getJSONObject(j);

                    String vId = vJson.getString("id");
                    String vType = vJson.getString("type");
                    String vMake = vJson.getString("make");
                    String vModel = vJson.getString("model");
                    String vPrice = vJson.getString("price");
                    String vStatus = vJson.getString("status");

                    Vehicle vehicle = new Vehicle(vType, vId, vPrice, vMake, vModel);
                    vehicle.setStatus(vStatus);

                    dealer.getVehicles().add(vehicle);
                }

                dealers.add(dealer);
            }

        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

        return dealers;
    }

    public static void saveAsJson(List<Dealer> dealers, String outputPath) {
        // Create JSON array to store all data
        JSONArray dealersArray = new JSONArray();

        // Iterate through each dealer -> get the id, name, etc...
        for (Dealer dealer : dealers) {
            // Store dealer data
            JSONObject dealerJson = new JSONObject();
            dealerJson.put("id", dealer.getId());
            dealerJson.put("name", dealer.getName());

            // Store vehicle data
            JSONArray vehiclesArray = new JSONArray();
            for (Vehicle v : dealer.getVehicles()) {
                JSONObject vehicleJson = new JSONObject();
                vehicleJson.put("id", v.getId());
                vehicleJson.put("type", v.getType());
                vehicleJson.put("make", v.getMake());
                vehicleJson.put("model", v.getModel());
                vehicleJson.put("price", v.getPrice());
                vehicleJson.put("status", v.getStatus());
                vehiclesArray.put(vehicleJson);
            }

            dealerJson.put("vehicles", vehiclesArray);

            // Wrap each dealer in a "Dealer" object
            JSONObject wrappedDealer = new JSONObject();
            wrappedDealer.put("Dealer", dealerJson);
            dealersArray.put(wrappedDealer);
        }

        // Root object with key "Dealers"
        JSONObject root = new JSONObject();
        root.put("Dealers", dealersArray);

        // Write the file with given path from user
        try (FileWriter file = new FileWriter(outputPath)) {
            file.write(root.toString(4)); // Add spacing of 4
            System.out.println("JSON file saved to: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error writing JSON: " + e.getMessage());
        }

    }
}
