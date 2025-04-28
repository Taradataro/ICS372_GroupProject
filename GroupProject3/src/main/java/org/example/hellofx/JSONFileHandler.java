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

    @Override
    public List<Dealer> getDealers(String inputPath) {
        List<Dealer> dealers = new ArrayList<>();
        try {
            String content = Files.readString(Paths.get(inputPath));
            JSONObject root = new JSONObject(content);
            JSONArray arr = root.getJSONArray("Dealers");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject dWrap = arr.getJSONObject(i).getJSONObject("Dealer");
                Dealer d = new Dealer(dWrap.getString("id"), dWrap.getString("name"));
                JSONArray vArr = dWrap.getJSONArray("vehicles");
                for (int j = 0; j < vArr.length(); j++) {
                    JSONObject vJ = vArr.getJSONObject(j);
                    Vehicle v = new Vehicle(
                            vJ.getString("type"),
                            vJ.getString("id"),
                            vJ.getString("price"),
                            vJ.getString("make"),
                            vJ.getString("model")
                    );
                    v.setStatus(vJ.getString("status"));
                    d.addVehicle(v);
                }
                dealers.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dealers;
    }

    public static void saveAsJson(List<Dealer> dealers, String outputPath) {
        JSONObject root = new JSONObject();
        JSONArray arr = new JSONArray();
        for (Dealer d : dealers) {
            JSONObject dJ = new JSONObject();
            dJ.put("id", d.getId());
            dJ.put("name", d.getName());
            JSONArray vArr = new JSONArray();
            for (Vehicle v : d.getVehicles()) {
                JSONObject vJ = new JSONObject();
                vJ.put("type", v.getType());
                vJ.put("id", v.getId());
                vJ.put("price", v.getPrice());
                vJ.put("make", v.getMake());
                vJ.put("model", v.getModel());
                vJ.put("status", v.getStatus());
                vArr.put(vJ);
            }
            dJ.put("vehicles", vArr);
            JSONObject wrap = new JSONObject();
            wrap.put("Dealer", dJ);
            arr.put(wrap);
        }
        root.put("Dealers", arr);

        try (FileWriter fw = new FileWriter(outputPath)) {
            fw.write(root.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
