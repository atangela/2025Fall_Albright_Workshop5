package org.example;

import java.util.Scanner;
import java.util.List;

public class UserInterface {
    private Dealership dealership;
    private Scanner scanner = new Scanner(System.in);

    public void display() {
        init();

        boolean running = true;
        while (running) {
            printMenu();
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Error: Please try again.");
                continue;
            }

            switch (choice) {
                case 1 -> processGetByPriceRequest();
                case 2 -> processGetByMakeModelRequest();
                case 3 -> processGetByYearRequest();
                case 4 -> processGetByColorRequest();
                case 5 -> processGetByMileageRequest();
                case 6 -> processGetByVehiclesByTypeRequest();
                case 7 -> processGetAllVehiclesRequest();
                case 8 -> processAddVehicleRequest();
                case 9 -> processRemoveVehicleRequest();
                case 10 -> sellOrLeaseVehicle();
                case 99 -> {
                    running = false;
                    System.out.println("Thank you.");
                }
                default -> System.out.println("Error: Please try again.");
            }
        }
    }

    private void init() {
        DealershipFileManager dealershipFileManager = new DealershipFileManager();
        this.dealership = dealershipFileManager.getDealership();

        if (this.dealership == null) {
            System.out.println("Error: Data not loaded.");
            System.exit(1);
        }
    }

    private void printMenu() {
        System.out.println("-----------Car Dealership Menu-----------");
        System.out.println("1. Find vehicles within a price range");
        System.out.println("2. Find vehicles by make / model");
        System.out.println("3. Find vehicles by year range");
        System.out.println("4. Find vehicles by color");
        System.out.println("5. Find vehicles by mileage range");
        System.out.println("6. Find vehicles by type(car, truck, suv, van)");
        System.out.println("7. List all vehicles");
        System.out.println("8. Add a vehicle");
        System.out.println("9. Remove a vehicle");
        System.out.println("10. Sell/Lease a vehicle");
        System.out.println("99. Exit");
        System.out.println("Enter choice: ");
    }

    private void processGetByPriceRequest() {
        double min = readDouble("Enter minimum price: ");
        double max = readDouble("Enter maximum price: ");
        List<Vehicle> vehicles = dealership.getVehiclesByPrice(min, max);
        displayVehicles(vehicles);
    }

    private void processGetByMakeModelRequest() {
        System.out.println("Enter make: ");
        String make = scanner.nextLine().trim();
        System.out.println("Enter model: ");
        String model = scanner.nextLine().trim();
        List<Vehicle> vehicles = dealership.getVehiclesByMakeModel(make,model);
        displayVehicles(vehicles);
    }

    private void processGetByYearRequest() {
        int minYear = (int) readDouble("Enter minimum year: ");
        int maxYear = (int) readDouble("Enter maximum year: ");
        List<Vehicle> vehicles = dealership.getVehiclesByYear(minYear, maxYear);
        displayVehicles(vehicles);
    }

    private void processGetByColorRequest() {
        System.out.println("Enter color: ");
        String color = scanner.nextLine().trim();
        List<Vehicle> vehicles = dealership.getVehiclesByColor(color);
        displayVehicles(vehicles);
    }

    private void processGetByMileageRequest() {
        int min = (int) readDouble("Enter minimum mileage: ");
        int max = (int) readDouble("Enter maximum mileage: ");
        List<Vehicle> vehicles = dealership.getVehiclesByMileage(min, max);
        displayVehicles(vehicles);
    }

    private void processGetByVehiclesByTypeRequest() {
        System.out.println("Enter type (car, truck, suv, van): ");
        String type = scanner.nextLine().trim().toLowerCase();
        List<Vehicle> vehicles = dealership.getVehiclesByType(type);
        displayVehicles(vehicles);
    }

    private void processGetAllVehiclesRequest() {
        List<Vehicle> vehicles = dealership.getAllVehicles();
        displayVehicles(vehicles);
    }

    private void processAddVehicleRequest() {
        System.out.println("---------Add a New Vehicle---------");

        int vin = (int) readDouble("Enter vin: ");
        String make = readString("Enter make: ");
        String model = readString("Enter model: ");
        int year = (int) readDouble("Enter year: ");
        String color = readString("Enter color: ");
        int mileage = (int) readDouble("Enter mileage: ");
        double price = readDouble("Enter price: ");
        String type = readString("Enter type (car, truck, suv, van): ");

        Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price);
                dealership.addVehicle(vehicle);
        System.out.println("Vehicle added.");
    }

    private void processRemoveVehicleRequest() {
        int vin = (int) readDouble("Enter vin of vehicle to remove: ");
        boolean removed = dealership.removeVehicleByVin(vin);

        if (removed) {
            System.out.println("Vehicle removed.");
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No inventory found.");
            return;
        }

        System.out.println("------------Vehicle List------------");
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Error: Please try again.");
            }
        }
    }

    private String readString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    private void sellOrLeaseVehicle() {
        System.out.println("Enter Vin of vehicle: ");
        String vin = scanner.nextLine();
        Vehicle vehicle = dealership.getVehicleByVin(Integer.parseInt(vin));

        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        System.out.println("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.println("Enter customer email: ");
        String email = scanner.nextLine();

        System.out.println("Please enter the letter (S) for Sale or (L) for Lease: ");
        String type = scanner.nextLine().toUpperCase();

        ContractFileManager contractFileManager = new ContractFileManager();

        if (type.equals("S")) {
            System.out.println("Finance the vehicle? Please enter the letter (Y) for Yes or (N) for No: ");
            boolean finance = scanner.nextLine().equalsIgnoreCase("Y");

            SalesContract contract = new SalesContract(getToday(), name, email, vehicle,finance);
            contractFileManager.saveContract(contract);
            dealership.removeVehicle(vehicle);

            System.out.println("Sale recorded successfully.");
        } else if (type.equals("L")) {
            int currentYear = java.time.LocalDate.now().getYear();
            if (currentYear - vehicle.getYear() > 3) {
                System.out.println("You cannot lease a vehicle over 3 years old.");
                return;
            }

            LeaseContract contract = new LeaseContract(getToday(), name, email, vehicle);
            contractFileManager.saveContract(contract);
            dealership.removeVehicle(vehicle);

            System.out.println("Lease recorded successfully.");
        }
    }

    private String getToday() {
        return java.time.LocalDate.now().toString().replace("-", "");
    }
}
