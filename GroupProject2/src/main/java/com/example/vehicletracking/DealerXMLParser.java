package com.example.vehicletracking;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DealerXMLParser {

    public List<Dealer> parseDealerXML(String filePath) {
        List<Dealer> dealers = new ArrayList<>();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList dealerNodes = doc.getElementsByTagName("Dealer");
            for (int i = 0; i < dealerNodes.getLength(); i++) {
                Node dealerNode = dealerNodes.item(i);
                if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element dealerElement = (Element) dealerNode;
                    String dealerId = dealerElement.getAttribute("id");
                    String dealerName = dealerElement.getElementsByTagName("Name").item(0).getTextContent();

                    List<Vehicle> vehicles = new ArrayList<>();
                    NodeList vehicleNodes = dealerElement.getElementsByTagName("Vehicle");
                    for (int j = 0; j < vehicleNodes.getLength(); j++) {
                        Node vehicleNode = vehicleNodes.item(j);
                        if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element vehicleElement = (Element) vehicleNode;
                            String vehicleId = vehicleElement.getAttribute("id");
                            String vehicleType = vehicleElement.getAttribute("type");
                            String vehiclePrice = vehicleElement.getElementsByTagName("Price").item(0).getTextContent();
                            String vehicleMake = vehicleElement.getElementsByTagName("Make").item(0).getTextContent();
                            String vehicleModel = vehicleElement.getElementsByTagName("Model").item(0).getTextContent();

                            vehicles.add(new Vehicle(vehicleType, vehicleId, vehiclePrice, vehicleMake, vehicleModel));
                        }
                    }

                    dealers.add(new Dealer(dealerName, dealerId, vehicles));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dealers;
    }
}
