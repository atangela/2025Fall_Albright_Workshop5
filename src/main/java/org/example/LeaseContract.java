package org.example;

public class LeaseContract extends Contract {
    private static double leaseRate = 0.04;
    private static int termMonths = 36;

    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicle) {
        super(date, customerName, customerEmail, vehicle);
    }

    public double getExpectedEndingValue() {
        return getVehicle().getPrice() * 0.5;
    }

    public double getLeaseFee() {
        return getVehicle().getPrice() * 0.07;
    }

    @Override
    public double getTotalPrice() {
        return getVehicle().getPrice() + getLeaseFee();
    }

    @Override
    public double getMonthlyPayment() {
        double capitalizedCost = getVehicle().getPrice() - getExpectedEndingValue();
        double monthlyRate = leaseRate / 12.0;
        return (capitalizedCost * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termMonths));
    }
}
