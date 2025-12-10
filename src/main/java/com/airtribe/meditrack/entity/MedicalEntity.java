package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

public abstract class MedicalEntity {

    protected Long id;
    protected LocalDateTime createdAt;

    public MedicalEntity(Long id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
    }

    // Default constructor
    public MedicalEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Force children to implement a string representation
    @Override
    public abstract String toString();
}
