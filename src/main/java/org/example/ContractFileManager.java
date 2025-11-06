package org.example;

import java.io.FileWriter;
import java.io.IOException;

public class ContractFileManager {
    private static String contractsFile = "contracts.csv";

    public void saveContract(Contract contract) {
        try (FileWriter writer = new FileWriter(contractsFile, true)) {
            Vehicle vehicle = contract.getVehicle();

            if (contract instanceof SalesContract sale) {
                writer.write(String.format(
                        "Sale|%s|%s|%s|%s|%d|%s|%s|%s|%s|%.2f|%.2f|%.2f|%.2f|%.2f|%s|%.2f\n",
                        sale.getDate(), sale.getCustomerName(), sale.getCustomerEmail(),
                        vehicle.getVin(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel(),
                        vehicle.getVehicleType(), vehicle.getColor(), vehicle.getOdometer(), vehicle.getPrice(),
                        sale.getSalesTax(), sale.getRecordingFee(), sale.getProcessingFee(),
                        sale.getTotalPrice(), sale.isFinanceOption() ? "Yes" : "No", sale.getMonthlyPayment()
                ));
            } else if (contract instanceof LeaseContract lease) {
                writer.write(String.format(
                        "Lease|%s|%s|%s|%s|%d|%s|%s|%s|%s|%.2f|%.2f|%.2f|%.2f|%.2f\n",
                        lease.getDate(), lease.getCustomerName(), lease.getCustomerEmail(),
                        vehicle.getVin(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel(),
                        vehicle.getVehicleType(), vehicle.getColor(), vehicle.getOdometer(), vehicle.getPrice(),
                        lease.getExpectedEndingValue(), lease.getLeaseFee(), lease.getTotalPrice(), lease.getMonthlyPayment()
                ));
            }

        } catch (IOException ex) {
            System.out.println("Error saving contract: " + ex.getMessage());
        }
    }
}
