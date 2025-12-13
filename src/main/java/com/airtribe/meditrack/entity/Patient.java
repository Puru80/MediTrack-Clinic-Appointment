package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;

public class Patient extends Person implements Searchable, Cloneable {

    private String medicalHistory;
    private String bloodGroup;
    private String address;

    public Patient(Long id, String name, int age, String email, String phoneNumber,
                   String medicalHistory, String bloodGroup, String address) {
        super(id, name, age, email, phoneNumber);
        this.setMedicalHistory(medicalHistory);
        this.setBloodGroup(bloodGroup);
        this.setAddress(address);
    }

    public Patient() {
        super();
    }
    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory != null ? medicalHistory : "";
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup != null ? bloodGroup : "";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address != null ? address : "";
    }

    @Override
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return false;
        }
        String lowerQuery = query.toLowerCase();
        return getName().toLowerCase().contains(lowerQuery) ||
               getEmail().toLowerCase().contains(lowerQuery) ||
               String.valueOf(getId()).equals(query.trim()) ||
               String.valueOf(getAge()).equals(query.trim());
    }

    @Override
    public Patient clone() {
        try {
            Patient cloned = (Patient) super.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Patient should be cloneable", e);
        }
    }

    @Override
    public String toString() {
        return String.format("Patient{id=%d, name='%s', age=%d, email='%s', bloodGroup='%s', address='%s'}", 
                getId(), getName(), getAge(), getEmail(), bloodGroup, address);
    }
}

