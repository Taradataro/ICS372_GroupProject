
package com.trackingcompany.main;

/**
 *
 * @author a
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.*;
import java.io.*;

public class jsonExport {
    public static void export(Dealership dealer, String path) {
        JSONArray inventory = new JSONArray();
        dealer.getVehicles().forEach(vehicle -> {
            JSONObject entry = new JSONObject();
            entry.put("vehicle_id", vehicle.getVehicleId());
            entry.put("vehicle_type", vehicle.getVehicleType());
            entry.put("price", vehicle.getPrice());
            // STEP 4: Include metadata
            entry.put("metadata", new JSONObject(vehicle.getMetadata()));
            inventory.add(entry);
        });
        String filePath = null;

        try(FileWriter file = new FileWriter(filePath)) {
            file.write(root.toJSONString()); 
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
