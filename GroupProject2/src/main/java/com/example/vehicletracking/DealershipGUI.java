package com.example.vehicletracking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class DealershipGUI {
    private Dealership dealership;

    public DealershipGUI(Dealership dealership) {
        this.dealership = dealership;

        // Create the main frame
        JFrame frame = new JFrame("Dealership Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create a panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10)); // Grid layout with 2 columns
        frame.add(panel);

        // Add components to the panel
        placeComponents(panel);

        // Display the frame
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        // Add Vehicle Button
        JButton addVehicleButton = new JButton("Add Vehicle");
        addVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddVehicleDialog();
            }
        });
        panel.add(addVehicleButton);

        // Loan Vehicle Button
        JButton loanVehicleButton = new JButton("Loan Vehicle");
        loanVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vehicleId = JOptionPane.showInputDialog("Enter Vehicle ID to Loan:");
                if (vehicleId != null && !vehicleId.isEmpty()) {
                    dealership.loanVehicle(vehicleId);
                }
            }
        });
        panel.add(loanVehicleButton);

        // Return Vehicle Button
        JButton returnVehicleButton = new JButton("Return Vehicle");
        returnVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vehicleId = JOptionPane.showInputDialog("Enter Vehicle ID to Return:");
                if (vehicleId != null && !vehicleId.isEmpty()) {
                    dealership.returnVehicle(vehicleId);
                }
            }
        });
        panel.add(returnVehicleButton);

        // Transfer Vehicle Button
        JButton transferVehicleButton = new JButton("Transfer Vehicle");
        transferVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vehicleId = JOptionPane.showInputDialog("Enter Vehicle ID to Transfer:");
                String targetDealershipId = JOptionPane.showInputDialog("Enter Target Dealership ID:");
                if (vehicleId != null && !vehicleId.isEmpty() && targetDealershipId != null && !targetDealershipId.isEmpty()) {
                    Dealership targetDealership = new Dealership(targetDealershipId, true);
                    dealership.transferVehicle(vehicleId, targetDealership);
                }
            }
        });
        panel.add(transferVehicleButton);

        // Print Inventory Button
        JButton printInventoryButton = new JButton("Print Inventory");
        printInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealership.printCurrentVehicles();
            }
        });
        panel.add(printInventoryButton);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(exitButton);
    }

    private void showAddVehicleDialog() {
        JTextField idField = new JTextField();
        JTextField manufacturerField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField typeField = new JTextField();

        JPanel addVehiclePanel = new JPanel(new GridLayout(0, 2));
        addVehiclePanel.add(new JLabel("Vehicle ID:"));
        addVehiclePanel.add(idField);
        addVehiclePanel.add(new JLabel("Manufacturer:"));
        addVehiclePanel.add(manufacturerField);
        addVehiclePanel.add(new JLabel("Model:"));
        addVehiclePanel.add(modelField);
        addVehiclePanel.add(new JLabel("Acquisition Date (yyyy-MM-dd):"));
        addVehiclePanel.add(dateField);
        addVehiclePanel.add(new JLabel("Price:"));
        addVehiclePanel.add(priceField);
        addVehiclePanel.add(new JLabel("Vehicle Type:"));
        addVehiclePanel.add(typeField);

        int result = JOptionPane.showConfirmDialog(null, addVehiclePanel, "Add Vehicle", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String manufacturer = manufacturerField.getText();
            String model = modelField.getText();
            LocalDate date = LocalDate.parse(dateField.getText());
            double price = Double.parseDouble(priceField.getText());
            String type = typeField.getText();

            Vehicle vehicle = new Vehicle(id, manufacturer, model, date, price, type);
            dealership.addVehicle(vehicle);

            // Update the JSON file
            jsonExport.exportDealershipToJson(dealership, "dealershipData.json");
        }
    }
}
