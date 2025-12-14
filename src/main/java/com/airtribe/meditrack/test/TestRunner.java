package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exceptions.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.time.LocalDateTime;

public class TestRunner {
    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final BillService billService = new BillService();

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("           MediTrack Test Runner");
        System.out.println("===========================================");

        try {
            // 1. Add Sample Patients
            System.out.println("\n--- 1. Adding Sample Patients ---");
            Patient patient1 = patientService.createPatient("Alice Smith", 30, "alice@example.com", "111-222-3333", "Hypertension", "A+", "123 Main St");
            Patient patient2 = patientService.createPatient("Bob Johnson", 45, "bob@example.com", "444-555-6666", "Diabetes", "B-", "456 Oak Ave");
            System.out.println("Added: " + patient1);
            System.out.println("Added: " + patient2);

            // 2. Add Sample Doctors
            System.out.println("\n--- 2. Adding Sample Doctors ---");
            Doctor doctor1 = doctorService.createDoctor("Dr. Carol White", 50, "carol@example.com", "777-888-9999", Specialization.CARDIOLOGY, 2500.0, 20);
            Doctor doctor2 = doctorService.createDoctor("Dr. David Green", 35, "david@example.com", "000-111-2222", Specialization.PEDIATRICS, 1800.0, 10);
            System.out.println("Added: " + doctor1);
            System.out.println("Added: " + doctor2);

            // 3. View All Patients and Doctors
            System.out.println("\n--- 3. Viewing All Patients ---");
            patientService.getAllPatients().forEach(System.out::println);
            System.out.println("\n--- 3. Viewing All Doctors ---");
            doctorService.getAllDoctors().forEach(System.out::println);

            // 4. Update Patient
            System.out.println("\n--- 4. Updating Patient 1 ---");
            patient1.setPhoneNumber("111-222-XXXX");
            patientService.updatePatient(patient1);
            System.out.println("Updated: " + patientService.getPatientById(patient1.getId()).orElse(null));

            // 5. Create Appointments
            System.out.println("\n--- 5. Creating Appointments ---");
            LocalDateTime apptTime1 = LocalDateTime.now().plusDays(5).withHour(10).withMinute(0);
            LocalDateTime apptTime2 = LocalDateTime.now().plusDays(2).withHour(14).withMinute(30);
            Appointment appt1 = appointmentService.createAppointment(patient1, doctor1, apptTime1, "Chest Pain", "Initial consultation");
            Appointment appt2 = appointmentService.createAppointment(patient2, doctor2, apptTime2, "Fever", "Routine check-up");
            System.out.println("Created: " + appt1);
            System.out.println("Created: " + appt2);

            // 6. View All Appointments
            System.out.println("\n--- 6. Viewing All Appointments ---");
            appointmentService.getAllAppointments().forEach(System.out::println);

            // 7. Confirm Appointment
            System.out.println("\n--- 7. Confirming Appointment 1 ---");
            appointmentService.confirmAppointment(appt1.getId());
            System.out.println("Confirmed: " + appointmentService.getAppointmentById(appt1.getId()).orElse(null));

            // 8. Generate Bills
            System.out.println("\n--- 8. Generating Bills ---");
            Bill bill1 = billService.generateBill(appt1, 100.0, 18.0); // Additional charges, 18% tax
            Bill bill2 = billService.generateBill(appt2, 0.0, 10.0); // No additional charges, 10% tax
            System.out.println("Generated: " + bill1);
            System.out.println("Generated: " + bill2);

            // 9. View All Bills
            System.out.println("\n--- 9. Viewing All Bills ---");
            billService.getAllBills().forEach(System.out::println);

            // 10. Mark Bill as Paid
            System.out.println("\n--- 10. Marking Bill 1 as Paid ---");
            billService.markBillAsPaid(bill1.getId(), "Card");
            System.out.println("Paid: " + billService.getBillById(bill1.getId()).orElse(null));

            // 11. View Revenue Statistics
            System.out.println("\n--- 11. Viewing Revenue Statistics ---");
            System.out.println("Total Revenue: " + billService.calculateTotalRevenue());
            System.out.println("Outstanding Amount: " + billService.calculateOutstandingAmount());
            System.out.println("Total Paid Bills: " + billService.getPaidBills().size());

            // 12. Delete Patient (and check cascade effects if applicable)
            System.out.println("\n--- 12. Deleting Patient 2 ---");
            patientService.deletePatient(patient2.getId());
            System.out.println("Patient 2 exists? " + patientService.getPatientById(patient2.getId()).isPresent());
            
            // Check if appointments for deleted patient are also removed/handled
            System.out.println("Appointment 2 exists? " + appointmentService.getAppointmentById(appt2.getId()).isPresent());


        } catch (InvalidDataException e) {
            System.err.println("Data validation error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n===========================================");
        System.out.println("           Test Runner Finished");
        System.out.println("===========================================");
    }
}
