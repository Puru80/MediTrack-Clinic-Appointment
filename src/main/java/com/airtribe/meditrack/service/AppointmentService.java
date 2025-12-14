package com.airtribe.meditrack.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exceptions.AppointmentNotFoundException;
import com.airtribe.meditrack.exceptions.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;


public class AppointmentService {
    private final DataStore<Appointment> appointmentStore;
    private final IdGenerator idGenerator;

   
    public AppointmentService() {
        this.appointmentStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
    }

    public Appointment createAppointment(Patient patient, Doctor doctor, 
                                        LocalDateTime appointmentDateTime,
                                        String symptoms, String notes) 
            throws InvalidDataException {
        if (patient == null) {
            throw new InvalidDataException("Patient cannot be null");
        }
        if (doctor == null) {
            throw new InvalidDataException("Doctor cannot be null");
        }
        if (appointmentDateTime == null) {
            throw new InvalidDataException("Appointment date and time cannot be null");
        }
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Appointment date and time cannot be in the past");
        }

        if (hasSchedulingConflict(doctor, appointmentDateTime)) {
            throw new InvalidDataException(
                "Doctor " + doctor.getName() + " is not available at " + appointmentDateTime
            );
        }

        Long id = idGenerator.generateId();
        Appointment appointment = new Appointment(
            id, patient, doctor, appointmentDateTime,
            AppointmentStatus.PENDING, notes, symptoms
        );

        appointmentStore.add(appointment);
        return appointment;
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentStore.findById(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentStore.getAll();
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        if (patientId == null) {
            return new ArrayList<>();
        }
        return appointmentStore.getAll().stream()
                .filter(apt -> apt.getPatient() != null && 
                              apt.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }


    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        if (doctorId == null) {
            return new ArrayList<>();
        }
        return appointmentStore.getAll().stream()
                .filter(apt -> apt.getDoctor() != null && 
                              apt.getDoctor().getId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        if (status == null) {
            return new ArrayList<>();
        }
        return appointmentStore.getAll().stream()
                .filter(apt -> apt.getStatus() == status)
                .collect(Collectors.toList());
    }


    public void confirmAppointment(Long appointmentId) 
            throws AppointmentNotFoundException, InvalidDataException {
        Appointment appointment = appointmentStore.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                    "Appointment with ID " + appointmentId + " not found"
                ));

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidDataException(
                "Only PENDING appointments can be confirmed. Current status: " + 
                appointment.getStatus()
            );
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentStore.update(appointment);
    }


    public void cancelAppointment(Long appointmentId, String cancellationReason) 
            throws AppointmentNotFoundException, InvalidDataException {
        Appointment appointment = appointmentStore.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                    "Appointment with ID " + appointmentId + " not found"
                ));

        if (!appointment.canBeCancelled()) {
            throw new InvalidDataException(
                "Appointment with status " + appointment.getStatus() + " cannot be cancelled"
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        if (cancellationReason != null && !cancellationReason.trim().isEmpty()) {
            appointment.setNotes(appointment.getNotes() + "\nCancellation Reason: " + cancellationReason);
        }
        appointmentStore.update(appointment);
    }

    public void rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime) 
            throws AppointmentNotFoundException, InvalidDataException {
        Appointment appointment = appointmentStore.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                    "Appointment with ID " + appointmentId + " not found"
                ));

        if (!appointment.canBeRescheduled()) {
            throw new InvalidDataException(
                "Appointment with status " + appointment.getStatus() + " cannot be rescheduled"
            );
        }

        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("New appointment date and time cannot be in the past");
        }

        if (hasSchedulingConflict(appointment.getDoctor(), newDateTime, appointmentId)) {
            throw new InvalidDataException(
                "Doctor is not available at " + newDateTime
            );
        }

        appointment.setAppointmentDateTime(newDateTime);
        appointment.setStatus(AppointmentStatus.RESCHEDULED);
        appointmentStore.update(appointment);
    }

    public void completeAppointment(Long appointmentId) 
            throws AppointmentNotFoundException, InvalidDataException {
        Appointment appointment = appointmentStore.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                    "Appointment with ID " + appointmentId + " not found"
                ));

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new InvalidDataException(
                "Only CONFIRMED appointments can be marked as completed"
            );
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentStore.update(appointment);
    }


    public void updateAppointmentNotes(Long appointmentId, String notes) 
            throws AppointmentNotFoundException, InvalidDataException {
        Appointment appointment = appointmentStore.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                    "Appointment with ID " + appointmentId + " not found"
                ));

        appointment.setNotes(notes);
        appointmentStore.update(appointment);
    }

    private boolean hasSchedulingConflict(Doctor doctor, LocalDateTime dateTime) {
        return hasSchedulingConflict(doctor, dateTime, null);
    }


    private boolean hasSchedulingConflict(Doctor doctor, LocalDateTime dateTime, 
                                         Long excludeAppointmentId) {
        return appointmentStore.getAll().stream()
                .filter(apt -> apt.getDoctor().getId().equals(doctor.getId()))
                .filter(apt -> apt.getStatus() == AppointmentStatus.CONFIRMED || 
                              apt.getStatus() == AppointmentStatus.PENDING)
                .filter(apt -> excludeAppointmentId == null || 
                              !apt.getId().equals(excludeAppointmentId))
                .anyMatch(apt -> {
                    long minutesDiff = Math.abs(
                        java.time.Duration.between(apt.getAppointmentDateTime(), dateTime).toMinutes()
                    );
                    return minutesDiff < 60;
                });
    }


    public int getAppointmentCount() {
        return appointmentStore.getAll().size();
    }


    public List<Appointment> getUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        return appointmentStore.getAll().stream()
                .filter(apt -> apt.getAppointmentDateTime().isAfter(now))
                .filter(apt -> apt.getStatus() == AppointmentStatus.CONFIRMED || 
                              apt.getStatus() == AppointmentStatus.PENDING)
                .sorted((a1, a2) -> a1.getAppointmentDateTime().compareTo(a2.getAppointmentDateTime()))
                .collect(Collectors.toList());
    }
}