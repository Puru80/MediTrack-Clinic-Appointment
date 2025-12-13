package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;

public class Doctor extends Person implements Searchable {

    private Specialization specialization;
    private double consultationFee;
    private int yearsOfExperience;

    public Doctor(Long id, String name, int age, String email, String phoneNumber,
                  Specialization specialization, double consultationFee, int yearsOfExperience) {
        super(id, name, age, email, phoneNumber);
        this.setSpecialization(specialization);
        this.setConsultationFee(consultationFee);
        this.setYearsOfExperience(yearsOfExperience);
    }

    public Doctor() {
        super();
    }
    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        if (specialization == null) {
            throw new IllegalArgumentException("Specialization cannot be null");
        }
        this.specialization = specialization;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        com.airtribe.meditrack.util.Validator.validateFee(consultationFee);
        this.consultationFee = consultationFee;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException("Years of experience cannot be negative");
        }
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return false;
        }
        String lowerQuery = query.toLowerCase();
        return getName().toLowerCase().contains(lowerQuery) ||
               getEmail().toLowerCase().contains(lowerQuery) ||
               specialization.getDisplayName().toLowerCase().contains(lowerQuery) ||
               String.valueOf(getId()).equals(query.trim());
    }

    @Override
    public String toString() {
        return String.format("Doctor{id=%d, name='%s', age=%d, specialization=%s, fee=%.2f, experience=%d years}", 
                getId(), getName(), getAge(), specialization.getDisplayName(), consultationFee, yearsOfExperience);
    }
}

