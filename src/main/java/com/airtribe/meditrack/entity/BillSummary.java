package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;


public final class BillSummary {
    private final Long billId;
    private final Long appointmentId;
    private final String patientName;
    private final String doctorName;
    private final LocalDateTime appointmentDate;
    private final double consultationFee;
    private final double additionalCharges;
    private final double taxRate;
    private final double taxAmount;
    private final double subtotal;
    private final double totalAmount;
    private final boolean isPaid;
    private final LocalDateTime billGeneratedDate;
    private final String description;

  
    public BillSummary(Long billId, Long appointmentId, String patientName, String doctorName,
                       LocalDateTime appointmentDate, double consultationFee, double additionalCharges,
                       double taxRate, double taxAmount, double subtotal, double totalAmount,
                       boolean isPaid, LocalDateTime billGeneratedDate, String description) {
        // Validate inputs
        if (billId == null) {
            throw new IllegalArgumentException("Bill ID cannot be null");
        }
        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        if (patientName == null || patientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be null or empty");
        }
        if (doctorName == null || doctorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be null or empty");
        }
        if (consultationFee < 0 || additionalCharges < 0 || totalAmount < 0) {
            throw new IllegalArgumentException("Financial values cannot be negative");
        }

        this.billId = billId;
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.consultationFee = consultationFee;
        this.additionalCharges = additionalCharges;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
        this.subtotal = subtotal;
        this.totalAmount = totalAmount;
        this.isPaid = isPaid;
        this.billGeneratedDate = billGeneratedDate != null ? billGeneratedDate : LocalDateTime.now();
        this.description = description != null ? description : "";
    }

  
    public static BillSummary fromBill(Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("Bill cannot be null");
        }
        
        Appointment appointment = bill.getAppointment();
        String patientName = appointment != null && appointment.getPatient() != null 
                ? appointment.getPatient().getName() : "Unknown";
        String doctorName = appointment != null && appointment.getDoctor() != null 
                ? appointment.getDoctor().getName() : "Unknown";
        LocalDateTime appointmentDate = appointment != null 
                ? appointment.getAppointmentDateTime() : null;

        return new BillSummary(
            bill.getId(),
            appointment != null ? appointment.getId() : null,
            patientName,
            doctorName,
            appointmentDate,
            bill.getConsultationFee(),
            bill.getAdditionalCharges(),
            bill.getTaxRate(),
            bill.getTaxAmount(),
            bill.getSubtotal(),
            bill.getTotalAmount(),
            bill.isPaid(),
            bill.getCreatedAt(),
            bill.getBillDescription()
        );
    }

    public Long getBillId() {
        return billId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

  
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public double getAdditionalCharges() {
        return additionalCharges;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public LocalDateTime getBillGeneratedDate() {
        return billGeneratedDate;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Creates a formatted string representation of the bill summary.
     * 
     * @return Formatted bill summary
     */
    public String getFormattedSummary() {
        return String.format(
            """
            Bill Summary
            ============
            Bill ID: %d
            Appointment ID: %d
            Patient: %s
            Doctor: %s
            Appointment Date: %s
            ------------
            Consultation Fee: ₹%.2f
            Additional Charges: ₹%.2f
            Subtotal: ₹%.2f
            Tax (%.2f%%): ₹%.2f
            ------------
            Total Amount: ₹%.2f
            Payment Status: %s
            Bill Generated: %s
            """,
            billId, appointmentId, patientName, doctorName, appointmentDate,
            consultationFee, additionalCharges, subtotal, taxRate, taxAmount,
            totalAmount, isPaid ? "PAID" : "UNPAID", billGeneratedDate
        );
    }

    @Override
    public String toString() {
        return String.format("BillSummary{billId=%d, patient='%s', doctor='%s', total=%.2f, paid=%s}",
                billId, patientName, doctorName, totalAmount, isPaid ? "Yes" : "No");
    }

    /**
     * Proper equals implementation for immutable class.
     * Two BillSummary objects are equal if all their fields are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BillSummary that = (BillSummary) obj;
        return Double.compare(that.consultationFee, consultationFee) == 0 &&
               Double.compare(that.additionalCharges, additionalCharges) == 0 &&
               Double.compare(that.taxRate, taxRate) == 0 &&
               Double.compare(that.totalAmount, totalAmount) == 0 &&
               isPaid == that.isPaid &&
               billId.equals(that.billId) &&
               appointmentId.equals(that.appointmentId) &&
               patientName.equals(that.patientName) &&
               doctorName.equals(that.doctorName);
    }

 
    @Override
    public int hashCode() {
        int result = billId.hashCode();
        result = 31 * result + appointmentId.hashCode();
        result = 31 * result + patientName.hashCode();
        result = 31 * result + doctorName.hashCode();
        result = 31 * result + Double.hashCode(totalAmount);
        return result;
    }
}
