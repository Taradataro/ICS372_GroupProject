package org.example.hellofx;

import java.util.List;

public class DealerSingleton {
    private static List<Dealer> dealers;

    // Private constructor to prevent instantiation
    private DealerSingleton() {
    }

    public static List<Dealer> getDealers(String filename) {
        // If dealers not exist -> create one. Otherwise, return only existing dealers
        if (dealers == null) {
            String extension = getFileExtension(filename);

            if (extension.equals("json")) {
                JSONFileHandler jsonFileHandler = new JSONFileHandler();
                dealers = jsonFileHandler.getDealers(filename);
            } else if (extension.equals("xml")) {
                ReadXMLFile readXMLFile = new ReadXMLFile();
                dealers = readXMLFile.getDealers(filename);
            } else {
                System.out.println("This file type is not supported!!!");
            }
        }
        return dealers;
    }

    // Check file extension: XML or JSON
    public static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public static void setDealers(List<Dealer> newDealers) {
        dealers = newDealers;
    }
}
