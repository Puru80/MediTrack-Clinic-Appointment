package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exceptions.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientService {

    private DataStore<Patient> patientStore;
    private IdGenerator idGenerator;

    public PatientService() {
        this.patientStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
    }

    public Patient createPatient(String name, int age, String email, String phoneNumber,
                                 String medicalHistory, String bloodGroup, String address)
            throws InvalidDataException {
        Long id = idGenerator.generateId();
        Patient patient = new Patient(id, name, age, email, phoneNumber, medicalHistory, bloodGroup, address);
        patientStore.add(patient);
        return patient;
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientStore.findById(id);
    }

    public List<Patient> getAllPatients() {
        return patientStore.getAll();
    }

    public void updatePatient(Patient patient) throws InvalidDataException {
        patientStore.update(patient);
    }

    public void deletePatient(Long id) throws InvalidDataException {
        patientStore.delete(id);
    }

    public Optional<Patient> searchPatient(Long id) {
        return patientStore.findById(id);
    }

    public List<Patient> searchPatient(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String searchName = name.toLowerCase().trim();
        return patientStore.getAll().stream()
                .filter(patient -> patient.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    public List<Patient> searchPatient(int age) {
        return patientStore.getAll().stream()
                .filter(patient -> patient.getAge() == age)
                .collect(Collectors.toList());
    }

    public List<Patient> searchPatients(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return patientStore.getAll().stream()
                .filter(patient -> patient.matches(query))
                .collect(Collectors.toList());
    }

    public int getPatientCount() {
        return patientStore.getAll().size();
    }
}

