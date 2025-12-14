package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

import com.airtribe.meditrack.interfaces.Payable;


public class Bill extends MedicalEntity implements Payable {
    private Appointment appointment;
    private double consultationFee;
    private double additionalCharges;
    private double taxRate; 
    private double totalAmount;
    private boolean isPaid;
    private LocalDateTime paymentDateTime;
    private String paymentMethod;
    private String billDescription;

    public Bill(Long id, Appointment appointment, double consultationFee, 
                double additionalCharges, double taxRate) {
        super(id);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        this.appointment = appointment;
        if (consultationFee < 0) {
            throw new IllegalArgumentException("Consultation fee cannot be negative");
        }
        this.consultationFee = consultationFee;
        this.setAdditionalCharges(additionalCharges);
        this.setTaxRate(taxRate);
        this.totalAmount = calculateTotalCost();
        this.isPaid = false;
        this.billDescription = "";
    }

 
    public Bill(Long id, Appointment appointment, double consultationFee, double taxRate) {
        this(id, appointment, consultationFee, 0.0, taxRate);
    }

 
    public Bill() {
        super();
        this.additionalCharges = 0.0;
        this.taxRate = 0.0;
        this.isPaid = false;
    }


    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        this.appointment = appointment;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        if (consultationFee < 0) {
            throw new IllegalArgumentException("Consultation fee cannot be negative");
        }
        this.consultationFee = consultationFee;
        this.totalAmount = calculateTotalCost();
    }

    public double getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(double additionalCharges) {
        if (additionalCharges < 0) {
            throw new IllegalArgumentException("Additional charges cannot be negative");
        }
        this.additionalCharges = additionalCharges;
        this.totalAmount = calculateTotalCost();
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        if (taxRate < 0 || taxRate > 100) {
            throw new IllegalArgumentException("Tax rate must be between 0 and 100");
        }
        this.taxRate = taxRate;
        this.totalAmount = calculateTotalCost();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
        if (paid && paymentDateTime == null) {
            paymentDateTime = LocalDateTime.now();
        }
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBillDescription() {
        return billDescription;
    }

    public void setBillDescription(String billDescription) {
        this.billDescription = billDescription != null ? billDescription : "";
    }

    @Override
    public double calculateTotalCost() {
        double subtotal = consultationFee + additionalCharges;
        double taxAmount = subtotal * (taxRate / 100.0);
        return subtotal + taxAmount;
    }

 
    public double getSubtotal() {
        return consultationFee + additionalCharges;
    }


    public double getTaxAmount() {
        return getSubtotal() * (taxRate / 100.0);
    }

    public void markAsPaid(String paymentMethod) {
        this.isPaid = true;
        this.paymentDateTime = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return String.format("Bill{id=%d, appointment=%d, subtotal=%.2f, tax=%.2f, total=%.2f, paid=%s}",
                getId(),
                (appointment != null && appointment.getId() != null) ? appointment.getId() : 0L,
                getSubtotal(),
                getTaxAmount(),
                totalAmount,
                isPaid ? "Yes" : "No");
    }
}
