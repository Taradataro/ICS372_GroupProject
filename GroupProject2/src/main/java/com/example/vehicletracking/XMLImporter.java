package com.example.vehicletracking;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;

public class XMLImporter {
    public static void importFromXML(String filePath, Dealership dealership) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList dealerList = doc.getElementsByTagName("Dealer");
            for (int i = 0; i < dealerList.getLength(); i++) {
                Node dealerNode = dealerList.item(i);
                if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element dealerElement = (Element) dealerNode;
                    String dealerId = dealerElement.getAttribute("id");
                    String dealerName = dealerElement.getElementsByTagName("Name").item(0).getTextContent();

                    NodeList vehicleList = dealerElement.getElementsByTagName("Vehicle");
                    for (int j = 0; j < vehicleList.getLength(); j++) {
                        Node vehicleNode = vehicleList.item(j);
                        if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element vehicleElement = (Element) vehicleNode;
                            String vehicleId = vehicleElement.getAttribute("id");
                            String type = vehicleElement.getAttribute("type");
                            String make = vehicleElement.getElementsByTagName("Make").item(0).getTextContent();
                            String model = vehicleElement.getElementsByTagName("Model").item(0).getTextContent();
                            double price = Double.parseDouble(vehicleElement.getElementsByTagName("Price").item(0).getTextContent());
                            LocalDate acquisitionDate = LocalDate.now(); // Assuming acquisition date is not in XML

                            Vehicle vehicle = new Vehicle(vehicleId, make, model, acquisitionDate, price, type);
                            dealership.addVehicle(vehicle);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error importing XML data: " + e.getMessage());
        }
    }
}
