package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exceptions.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DoctorService {

    private DataStore<Doctor> doctorStore;
    private IdGenerator idGenerator;

    public DoctorService() {
        this.doctorStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
    }

    public Doctor createDoctor(String name, int age, String email, String phoneNumber,
                               Specialization specialization, double consultationFee, int yearsOfExperience)
            throws InvalidDataException {
        Long id = idGenerator.generateId();
        Doctor doctor = new Doctor(id, name, age, email, phoneNumber, specialization, consultationFee, yearsOfExperience);
        doctorStore.add(doctor);
        return doctor;
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorStore.findById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorStore.getAll();
    }

    public void updateDoctor(Doctor doctor) throws InvalidDataException {
        doctorStore.update(doctor);
    }

    public void deleteDoctor(Long id) throws InvalidDataException {
        doctorStore.delete(id);
    }

    public List<Doctor> searchDoctorsBySpecialization(Specialization specialization) {
        return doctorStore.getAll().stream()
                .filter(doctor -> doctor.getSpecialization() == specialization)
                .collect(Collectors.toList());
    }

    public List<Doctor> searchDoctors(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return doctorStore.getAll().stream()
                .filter(doctor -> doctor.matches(query))
                .collect(Collectors.toList());
    }

    public int getDoctorCount() {
        return doctorStore.getAll().size();
    }
}

