package org.example.dealershipapplication;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Demonstrates how to parse XML and import dealership data into a DealershipManager.
 * Corrected to ensure that variables referenced in the orElseGet lambda are final/effectively final.
 */
public class XMLImporter {

    public static void importFromXML(String filePath, DealershipManager manager) {
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new File(filePath));

            NodeList dealerNodes = doc.getElementsByTagName("Dealer");
            for (int i = 0; i < dealerNodes.getLength(); i++) {
                Element dealerElem = (Element) dealerNodes.item(i);

                // Get or assign a fallback Dealer ID
                String dealerId = dealerElem.getAttribute("id");
                if (dealerId == null || dealerId.isBlank()) {
                    dealerId = "UNKNOWN-" + i;
                }

                // Must use a final or effectively final variable inside the lambda
                final String finalDealerId = dealerId;

                // Attempt to find existing dealer or create a new one
                Optional<Dealership> existing = manager.getDealershipById(finalDealerId);
                Dealership dealership = existing.orElseGet(() -> {
                    Dealership d = new Dealership(finalDealerId, true);
                    manager.addDealership(d);
                    return d;
                });

                // Set the name from <Name> if present
                NodeList nameNodes = dealerElem.getElementsByTagName("Name");
                if (nameNodes.getLength() > 0) {
                    dealership.setDealerName(nameNodes.item(0).getTextContent());
                }

                // Now read <Vehicle> elements
                NodeList vehicleNodes = dealerElem.getElementsByTagName("Vehicle");
                for (int j = 0; j < vehicleNodes.getLength(); j++) {
                    Element vehicleElem = (Element) vehicleNodes.item(j);

                    double price = parseVehiclePrice(vehicleElem);
                    String vehId = vehicleElem.getAttribute("id");
                    String type = vehicleElem.getAttribute("type");

                    // Extract Make and Model from tags
                    String make = getTagValue(vehicleElem, "Make");
                    String model = getTagValue(vehicleElem, "Model");

                    // Acquisition date is not in XML, so default to today's date
                    LocalDate date = LocalDate.now();

                    // Create the Vehicle object and add it to this dealership
                    Vehicle vehicle = new Vehicle(vehId, make, model, date, price, type);
                    dealership.addVehicle(vehicle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error importing from XML: " + e.getMessage());
        }
    }

    private static double parseVehiclePrice(Element vehicle) {
        NodeList priceNodes = vehicle.getElementsByTagName("Price");
        if (priceNodes.getLength() == 0) {
            return 0.0;
        }
        Element priceElement = (Element) priceNodes.item(0);
        double price = Double.parseDouble(priceElement.getTextContent());

        // Handle possible "unit" attribute (pounds -> convert to dollars)
        String unit = priceElement.getAttribute("unit");
        if ("pounds".equalsIgnoreCase(unit)) {
            return price * 1.25;  // Example conversion rate
        }
        return price;
    }

    /**
     * Utility method to safely extract a tag's text value from a parent element.
     */
    private static String getTagValue(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
}
