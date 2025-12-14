package com.airtribe.meditrack.entity;


public enum AppointmentStatus {
    PENDING("Pending", "Appointment is scheduled but not yet confirmed"),
    CONFIRMED("Confirmed", "Appointment has been confirmed by both parties"),
    CANCELLED("Cancelled", "Appointment has been cancelled"),
    COMPLETED("Completed", "Appointment has been successfully completed"),
    NO_SHOW("No Show", "Patient did not attend the scheduled appointment"),
    RESCHEDULED("Rescheduled", "Appointment has been moved to a different time");

    private final String displayName;
    private final String description;

    /**
     * Private constructor for the enum.
     * 
     * @param displayName User-friendly name for the status
     * @param description Detailed description of the status
     */
    AppointmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Gets the display name of the status.
     * 
     * @return Human-readable status name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the description of the status.
     * 
     * @return Detailed status description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if the appointment can be cancelled based on current status.
     * 
     * @return true if cancellation is allowed, false otherwise
     */
    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED || this == RESCHEDULED;
    }

    /**
     * Checks if the appointment can be rescheduled based on current status.
     * 
     * @return true if rescheduling is allowed, false otherwise
     */
    public boolean canBeRescheduled() {
        return this == PENDING || this == CONFIRMED;
    }

    @Override
    public String toString() {
        return displayName;
    }
}