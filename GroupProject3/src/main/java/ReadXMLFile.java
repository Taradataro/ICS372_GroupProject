import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadXMLFile {
    public static final String FILENAME = "src/main/resources/Dealer.xml";
    public static List<Dealer> getDealers() {
        // List to store dealers
        List<Dealer> dealers = new ArrayList<>();

        // Document Builder Factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Document Builder
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the xml file: read the file and extract information
            Document document =  builder.parse(new File(FILENAME));

            // Create Node list to store each dealer
            NodeList dealerList = document.getElementsByTagName("Dealer");
            System.out.println("Total Dealers: " + dealerList.getLength() + "\n");

            // Iterate through the Node list and get Node element and attributes
            for (int i = 0; i < dealerList.getLength(); i++) {
                Node node = dealerList.item(i);
                // Check node type
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    System.out.println("Dealership ID: " + id);

                    String name = element.getElementsByTagName("Name").item(0).getTextContent();

                    // Create a list of vehicles
                    List<Vehicle> vehicles = new ArrayList<>();

                    // Get list of vehicle
                    NodeList vehicleList = element.getElementsByTagName("Vehicle");
                    System.out.println("Total vehicle per dealer: " + vehicleList.getLength());
                    for (int j = 0; j < vehicleList.getLength(); j++) {
                        // Get all vehicle content
                        Element vehicleElement = (Element) vehicleList.item(j);
                        String type = vehicleElement.getAttribute("type");
                        String vehicleId = vehicleElement.getAttribute("id");
                        Element priceElement = (Element) vehicleElement.getElementsByTagName("Price").item(0);
                        String price = priceElement.getTextContent();
                        String unit = priceElement.getAttribute("unit");
                        String unitPrice = "£ " + price;
                        if (unit.equals("dollars")) {
                            unitPrice = "$ " + price;
                        }
                        String make = vehicleElement.getElementsByTagName("Make").item(0).getTextContent();
                        String model = vehicleElement.getElementsByTagName("Model").item(0).getTextContent();

                        // Create a new vehicle object
                        Vehicle vehicle = new Vehicle(type, vehicleId, unitPrice, make, model);
                        vehicles.add(vehicle);
                    }
                    // Create dealer object and add vehicle to the list
                    Dealer dealer = new Dealer(id, name);
                    dealer.setVehicles(vehicles);
                    dealers.add(dealer);

                    // Print out the dealer and vehicles information
                    for (Vehicle v : vehicles) {
                        System.out.println("Vehicle: " + v.getMake() + " " + v.getModel() + " (" + v.getType() + ")");
                    }
                    System.out.println();

                }

            }


        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return dealers;

    }
}