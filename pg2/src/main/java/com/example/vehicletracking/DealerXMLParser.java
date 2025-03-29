package com.example.vehicletracking;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class DealerXMLParser {

    // Method to parse the dealer data from an XML file
    public List<Dealer> parseDealerXML(String xmlFilePath) {
        List<Dealer> dealers = new ArrayList<>();
        try {
            // Parse XML and extract dealer and vehicle data
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            NodeList dealerNodes = doc.getElementsByTagName("Dealer");

            for (int i = 0; i < dealerNodes.getLength(); i++) {
                Node dealerNode = dealerNodes.item(i);
                if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element dealerElement = (Element) dealerNode;
                    String dealerName = dealerElement.getElementsByTagName("Name").item(0).getTextContent();

                    Dealer dealer = new Dealer(dealerName);

                    NodeList vehicleNodes = dealerElement.getElementsByTagName("Vehicle");
                    for (int j = 0; j < vehicleNodes.getLength(); j++) {
                        Node vehicleNode = vehicleNodes.item(j);
                        if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element vehicleElement = (Element) vehicleNode;
                            String type = vehicleElement.getAttribute("type");
                            String id = vehicleElement.getAttribute("id");
                            String price = vehicleElement.getElementsByTagName("Price").item(0).getTextContent();
                            String make = vehicleElement.getElementsByTagName("Make").item(0).getTextContent();
                            String model = vehicleElement.getElementsByTagName("Model").item(0).getTextContent();

                            // Set a default value for status, e.g., "Available"
                            String status = "Available";

                            // Create the Vehicle object
                            Vehicle vehicle = new Vehicle(type, id, price, make, model);
                            dealer.addVehicle(vehicle);
                        }
                    }
                    dealers.add(dealer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dealers;
    }

    // Method to save the dealer data to an XML file
    public void saveDealerXML(String fileName, List<Dealer> dealers) {
        try {
            // Create a new Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Create root element <Dealers>
            Element rootElement = doc.createElement("Dealers");
            doc.appendChild(rootElement);

            // Iterate through each dealer and their vehicles
            for (Dealer dealer : dealers) {
                Element dealerElement = doc.createElement("Dealer");

                // Add <Name> element for the dealer's name
                Element nameElement = doc.createElement("Name");
                nameElement.appendChild(doc.createTextNode(dealer.getName()));
                dealerElement.appendChild(nameElement);

                // Add <Vehicle> elements for each vehicle
                for (Vehicle vehicle : dealer.getVehicles()) {
                    Element vehicleElement = doc.createElement("Vehicle");

                    // Set attributes and elements for the vehicle
                    vehicleElement.setAttribute("type", vehicle.getType());
                    vehicleElement.setAttribute("id", vehicle.getId());

                    Element priceElement = doc.createElement("Price");
                    priceElement.appendChild(doc.createTextNode(vehicle.getPrice()));
                    vehicleElement.appendChild(priceElement);

                    Element makeElement = doc.createElement("Make");
                    makeElement.appendChild(doc.createTextNode(vehicle.getMake()));
                    vehicleElement.appendChild(makeElement);

                    Element modelElement = doc.createElement("Model");
                    modelElement.appendChild(doc.createTextNode(vehicle.getModel()));
                    vehicleElement.appendChild(modelElement);

                    dealerElement.appendChild(vehicleElement);
                }

                // Add the dealer element to the root
                rootElement.appendChild(dealerElement);
            }

            // Write the content to an XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

