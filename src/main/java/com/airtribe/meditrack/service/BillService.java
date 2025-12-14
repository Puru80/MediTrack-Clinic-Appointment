package com.airtribe.meditrack.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.exceptions.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;


public class BillService {
    private final DataStore<Bill> billStore;
    private final IdGenerator idGenerator;
    private static final double DEFAULT_TAX_RATE = 18.0; 


    public BillService() {
        this.billStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
    }

    public Bill generateBill(Appointment appointment) throws InvalidDataException {
        return generateBill(appointment, 0.0, DEFAULT_TAX_RATE);
    }

   
    public Bill generateBill(Appointment appointment, double additionalCharges) 
            throws InvalidDataException {
        return generateBill(appointment, additionalCharges, DEFAULT_TAX_RATE);
    }

   
    public Bill generateBill(Appointment appointment, double additionalCharges, double taxRate) 
            throws InvalidDataException {
        if (appointment == null) {
            throw new InvalidDataException("Appointment cannot be null");
        }
        if (appointment.getDoctor() == null) {
            throw new InvalidDataException("Appointment must have a doctor assigned");
        }
        if (additionalCharges < 0) {
            throw new InvalidDataException("Additional charges cannot be negative");
        }
        if (taxRate < 0 || taxRate > 100) {
            throw new InvalidDataException("Tax rate must be between 0 and 100");
        }

        Optional<Bill> existingBill = getBillByAppointment(appointment.getId());
        if (existingBill.isPresent()) {
            throw new InvalidDataException(
                "Bill already exists for appointment ID " + appointment.getId()
            );
        }

        double consultationFee = appointment.getDoctor().getConsultationFee();

        Long id = idGenerator.generateId();
        Bill bill = new Bill(id, appointment, consultationFee, additionalCharges, taxRate);
        
        String description = String.format(
            "Medical consultation with Dr. %s (%s) for patient %s",
            appointment.getDoctor().getName(),
            appointment.getDoctor().getSpecialization().getDisplayName(),
            appointment.getPatient().getName()
        );
        bill.setBillDescription(description);

        billStore.add(bill);
        return bill;
    }

    public Optional<Bill> getBillById(Long id) {
        return billStore.findById(id);
    }

    public List<Bill> getAllBills() {
        return billStore.getAll();
    }

    public Optional<Bill> getBillByAppointment(Long appointmentId) {
        if (appointmentId == null) {
            return Optional.empty();
        }
        return billStore.getAll().stream()
                .filter(bill -> bill.getAppointment() != null && 
                               bill.getAppointment().getId().equals(appointmentId))
                .findFirst();
    }


    public List<Bill> getBillsByPatient(Long patientId) {
        if (patientId == null) {
            return new ArrayList<>();
        }
        return billStore.getAll().stream()
                .filter(bill -> bill.getAppointment() != null &&
                               bill.getAppointment().getPatient() != null &&
                               bill.getAppointment().getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }


    public List<Bill> getUnpaidBills() {
        return billStore.getAll().stream()
                .filter(bill -> !bill.isPaid())
                .collect(Collectors.toList());
    }


    public List<Bill> getPaidBills() {
        return billStore.getAll().stream()
                .filter(Bill::isPaid)
                .collect(Collectors.toList());
    }

    public void markBillAsPaid(Long billId, String paymentMethod) throws InvalidDataException {
        Bill bill = billStore.findById(billId)
                .orElseThrow(() -> new InvalidDataException("Bill with ID " + billId + " not found"));

        if (bill.isPaid()) {
            throw new InvalidDataException("Bill is already marked as paid");
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new InvalidDataException("Payment method cannot be empty");
        }

        bill.markAsPaid(paymentMethod);
        billStore.update(bill);
    }


    public void updateAdditionalCharges(Long billId, double additionalCharges) 
            throws InvalidDataException {
        Bill bill = billStore.findById(billId)
                .orElseThrow(() -> new InvalidDataException("Bill with ID " + billId + " not found"));

        if (bill.isPaid()) {
            throw new InvalidDataException("Cannot update paid bill");
        }

        bill.setAdditionalCharges(additionalCharges);
        billStore.update(bill);
    }


    public BillSummary generateBillSummary(Long billId) throws InvalidDataException {
        Bill bill = billStore.findById(billId)
                .orElseThrow(() -> new InvalidDataException("Bill with ID " + billId + " not found"));

        return BillSummary.fromBill(bill);
    }

    public List<BillSummary> generateAllBillSummaries() {
        return billStore.getAll().stream()
                .map(BillSummary::fromBill)
                .collect(Collectors.toList());
    }


    public List<BillSummary> generateBillSummariesByPatient(Long patientId) {
        return getBillsByPatient(patientId).stream()
                .map(BillSummary::fromBill)
                .collect(Collectors.toList());
    }

 
    public double calculateTotalRevenue() {
        return billStore.getAll().stream()
                .filter(Bill::isPaid)
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }


    public double calculateOutstandingAmount() {
        return billStore.getAll().stream()
                .filter(bill -> !bill.isPaid())
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }


    public int getBillCount() {
        return billStore.getAll().size();
    }

    public List<Bill> getBillsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return new ArrayList<>();
        }
        return billStore.getAll().stream()
                .filter(bill -> {
                    LocalDateTime billDate = bill.getCreatedAt();
                    return !billDate.isBefore(startDate) && !billDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }


    public void deleteBill(Long billId) throws InvalidDataException {
        Bill bill = billStore.findById(billId)
                .orElseThrow(() -> new InvalidDataException("Bill with ID " + billId + " not found"));

        if (bill.isPaid()) {
            throw new InvalidDataException("Cannot delete a paid bill");
        }

        billStore.delete(billId);
    }
}
