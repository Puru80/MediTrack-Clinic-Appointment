package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;


public class Appointment extends MedicalEntity implements Cloneable {
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String notes;
    private String symptoms;

    public Appointment(Long id, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime,
                       AppointmentStatus status, String notes, String symptoms) {
        super(id);
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        if (appointmentDateTime == null) {
            throw new IllegalArgumentException("Appointment date and time cannot be null");
        }
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date and time cannot be in the past");
        }
        if (status == null) {
            throw new IllegalArgumentException("Appointment status cannot be null");
        }
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.notes = notes != null ? notes : "";
        this.symptoms = symptoms != null ? symptoms : "";
    }

    /**
     * Default constructor.
     */
    public Appointment() {
        super();
        this.status = AppointmentStatus.PENDING;
    }

    // Getters and Setters with validation

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        this.doctor = doctor;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        if (appointmentDateTime == null) {
            throw new IllegalArgumentException("Appointment date and time cannot be null");
        }
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date and time cannot be in the past");
        }
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Appointment status cannot be null");
        }
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms != null ? symptoms : "";
    }

    
    @Override
    public Appointment clone() {
        try {
            Appointment cloned = (Appointment) super.clone();
            
            if (this.patient != null) {
                cloned.patient = this.patient.clone();
            }
            
            if (this.doctor != null) {
                cloned.doctor = new Doctor(
                    this.doctor.getId(),
                    this.doctor.getName(),
                    this.doctor.getAge(),
                    this.doctor.getEmail(),
                    this.doctor.getPhoneNumber(),
                    this.doctor.getSpecialization(),
                    this.doctor.getConsultationFee(),
                    this.doctor.getYearsOfExperience()
                );
            }
            
            
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Appointment should be cloneable", e);
        }
    }

    public boolean canBeCancelled() {
        return status.canBeCancelled();
    }


    public boolean canBeRescheduled() {
        return status.canBeRescheduled();
    }

    @Override
    public String toString() {
        return String.format("Appointment{id=%d, patient=%s, doctor=%s, dateTime=%s, status=%s, symptoms='%s'}",
                getId(), 
                patient != null ? patient.getName() : "N/A",
                doctor != null ? doctor.getName() : "N/A",
                appointmentDateTime,
                status.getDisplayName(),
                symptoms);
    }
}
