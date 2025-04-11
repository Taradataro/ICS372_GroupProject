import java.util.List;

public class Dealer {
    // Fields
    private String id;
    private String name;
    private List<Vehicle> vehicles;

    public Dealer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Setter for vehicles
    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    // Getter methods
    public String getId() {
        return id;
    }
    public String getName() {return name;}

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dealer ID: ").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Vehicles:\n");
        for (Vehicle vehicle : vehicles) {
            sb.append("  - ").append(vehicle.getMake()).append(" ").append(vehicle.getModel())
                    .append(" (").append(vehicle.getType()).append(")").append("\n");
        }
        return sb.toString();
    }

}