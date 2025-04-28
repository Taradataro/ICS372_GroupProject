package org.example.hellofx;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadXMLFile implements FileHandler {

    @Override
    public List<Dealer> getDealers(String fileName) {
        List<Dealer> dealers = new ArrayList<>();
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new File(fileName));
            NodeList dNodes = doc.getElementsByTagName("Dealer");

            for (int i = 0; i < dNodes.getLength(); i++) {
                Element dEl = (Element) dNodes.item(i);
                Dealer d = new Dealer(
                        dEl.getAttribute("id"),
                        dEl.getElementsByTagName("Name").item(0).getTextContent()
                );
                NodeList vNodes = dEl.getElementsByTagName("Vehicle");
                for (int j = 0; j < vNodes.getLength(); j++) {
                    Element vEl = (Element) vNodes.item(j);
                    String unit = ((Element)vEl.getElementsByTagName("Price").item(0)).getAttribute("unit");
                    String price = ((Element)vEl.getElementsByTagName("Price").item(0)).getTextContent();
                    String pfx = unit.equals("dollars") ? "$ " : "£ ";
                    Vehicle v = new Vehicle(
                            vEl.getAttribute("type"),
                            vEl.getAttribute("id"),
                            pfx + price,
                            vEl.getElementsByTagName("Make").item(0).getTextContent(),
                            vEl.getElementsByTagName("Model").item(0).getTextContent()
                    );
                    d.addVehicle(v);
                }
                dealers.add(d);
            }
        } catch (ParserConfigurationException|SAXException|IOException e) {
            e.printStackTrace();
        }
        return dealers;
    }
}
