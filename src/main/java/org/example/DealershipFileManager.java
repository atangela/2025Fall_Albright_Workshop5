package org.example;

import java.io.*;

public class DealershipFileManager {
    private static final String CAR_KINGS_DEALERSHIP = "src/main/resources/CarKingsInventory.csv";

    public Dealership getDealership() {
        Dealership dealership = null;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CAR_KINGS_DEALERSHIP))) {
        String line = bufferedReader.readLine();
        if (line != null) {
            String[] parts = line.split("\\|");
            dealership = new Dealership(parts[0], parts[1], parts[2]);
        }

        if (dealership == null) {
            System.out.println("Info not found");
            return null;
        }

        while ((line = bufferedReader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] p = line.split("\\|");
            if (p.length < 8) continue;
            Vehicle vehicle = new Vehicle(
                    Integer.parseInt(p[0]),
                    Integer.parseInt(p[1]),
                    p[2],
                    p[3],
                    p[4],
                    p[5],
                    Integer.parseInt(p[6]),
                    Double.parseDouble(p[7])
            );
            dealership.addVehicle(vehicle);
        }
    } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return dealership;
    }

    public void saveDealership(Dealership dealership) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CAR_KINGS_DEALERSHIP))) {
            writer.printf("%s|%s|%s%n", dealership.getName(), dealership.getAddress(), dealership.getPhone());
            for (Vehicle vehicle : dealership.getAllVehicles()) {
                writer.printf("%d|%d|%s|%s|%s|%s|%d|%.2f%n",
                    vehicle.getVin(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel(), vehicle.getVehicleType(), vehicle.getColor(), vehicle.getOdometer(), vehicle.getPrice());
            }
            System.out.println("Dealership saved.");
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
