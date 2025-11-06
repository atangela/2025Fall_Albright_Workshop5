package org.example;

public class SalesContract extends Contract {
    private static double salesTaxRate = 0.05;
    private static double recordingFee = 100.0;

    private double processingFee;
    private boolean financeOption;

    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicle, boolean financeOption) {
        super(date, customerName, customerEmail, vehicle);
        this.processingFee = (vehicle.getPrice() < 10000) ? 295 : 495;
        this.financeOption = financeOption;

    }

    public double getSalesTax() {
        return getVehicle().getPrice() * salesTaxRate;
    }

    public double getRecordingFee() {
        return recordingFee;
    }

    public double getProcessingFee() {
        return processingFee;
    }

    public boolean isFinanceOption() {
        return financeOption;
    }

    @Override
    public double getTotalPrice() {
        return getVehicle().getPrice() + getSalesTax() + getRecordingFee() + getProcessingFee();
    }

    @Override
    public double getMonthlyPayment() {
        if (!financeOption) return 0.0;

        double price = getTotalPrice();
        double annualRate;
        int months;

        if (price >= 10000) {
            annualRate = 0.0425;
            months = 48;
        } else {
            annualRate = 0.0525;
            months = 24;
        }

        double monthlyRate = annualRate / 12.0;
        return (price * monthlyRate) / (1 - Math.pow(1 + monthlyRate,-months));
    }
}
