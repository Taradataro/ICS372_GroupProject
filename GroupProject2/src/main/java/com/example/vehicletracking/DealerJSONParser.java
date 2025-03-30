package com.example.vehicletracking;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DealerJSONParser {
    public List<Vehicle> parseCarInventoryJSON(String jsonFilePath) {
        List<Vehicle> vehicles = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray carInventory = jsonObject.getJSONArray("car_inventory");

            for (int i = 0; i < carInventory.length(); i++) {
                JSONObject vehicleObject = carInventory.getJSONObject(i);
                String type = vehicleObject.getString("vehicle_type");
                String id = vehicleObject.getString("vehicle_id");
                String price = String.valueOf(vehicleObject.getInt("price"));
                String make = vehicleObject.getString("vehicle_manufacturer");
                String model = vehicleObject.getString("vehicle_model");

                Vehicle vehicle = new Vehicle(type, id, price, make, model);
                vehicles.add(vehicle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public void saveToInfJson(List<Vehicle> vehicles, String outputPath) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray carInventory = new JSONArray();

            for (Vehicle vehicle : vehicles) {
                JSONObject vehicleJson = new JSONObject();
                vehicleJson.put("vehicle_type", vehicle.getType());
                vehicleJson.put("vehicle_id", vehicle.getId());
                vehicleJson.put("price", Integer.parseInt(vehicle.getPrice()));
                vehicleJson.put("vehicle_manufacturer", vehicle.getMake());
                vehicleJson.put("vehicle_model", vehicle.getModel());
                carInventory.put(vehicleJson);
            }

            jsonObject.put("car_inventory", carInventory);

            // Write to file
            Files.write(Paths.get(outputPath), jsonObject.toString(2).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}