package org.example.dealershipapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages multiple Dealerships, allowing us to have multiple dealers
 * in the same application, track them, and handle operations like transfer.
 */
public class DealershipManager {
    private final List<Dealership> dealerships;

    public DealershipManager() {
        this.dealerships = new ArrayList<>();
    }

    public void addDealership(Dealership dealership) {
        // Only add if we don't already have a dealership with the same ID
        if (getDealershipById(dealership.getId()).isEmpty()) {
            dealerships.add(dealership);
        }
    }

    public Optional<Dealership> getDealershipById(String id) {
        return dealerships.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    /**
     * Transfer a vehicle from one dealer to another by vehicle ID.
     */
    public boolean transferVehicle(String vehicleId, String fromDealerId, String toDealerId) {
        Optional<Dealership> fromOpt = getDealershipById(fromDealerId);
        Optional<Dealership> toOpt = getDealershipById(toDealerId);
        if (fromOpt.isEmpty() || toOpt.isEmpty()) {
            return false; // One or both dealers not found
        }

        Dealership from = fromOpt.get();
        Dealership to = toOpt.get();

        // Check if 'to' is receiving
        if (!to.isReceivingEnabled()) {
            System.out.println("Cannot transfer - the receiving dealer is disabled.");
            return false;
        }

        // Find the vehicle in 'from'
        Optional<Vehicle> vehicleOpt = from.getVehicles().stream()
                .filter(v -> v.getId().equals(vehicleId))
                .findFirst();
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            // Remove from the old dealer
            from.removeVehicle(vehicleId);
            // Add to the new dealer
            to.addVehicle(vehicle);
            return true;
        }
        return false;
    }

    public List<Dealership> getAllDealerships() {
        return new ArrayList<>(dealerships);
    }
}
