package com.airtribe.meditrack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exceptions.AppointmentNotFoundException;
import com.airtribe.meditrack.exceptions.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

public class Main {
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final BillService billService = new BillService();
    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   Welcome to MediTrack System");
        System.out.println("===========================================");
        
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ", scanner);
                
                switch (choice) {
                    case 1 -> {
                        // Patient Management (handled by Role 1)
                    }
                    case 2 -> {
                        // Doctor Management (handled by Role 1)
                    }
                    case 3 -> handleAppointmentMenu(scanner);
                    case 4 -> handleBillingMenu(scanner);
                    case 5 -> {
                        running = false;
                        System.out.println("Thank you for using MediTrack. Goodbye!");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n===========================================");
        System.out.println("              MAIN MENU");
        System.out.println("===========================================");
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("3. Appointment Management");
        System.out.println("4. Billing Management");
        System.out.println("5. Exit");
        System.out.println("===========================================");
    }

    // ======================== APPOINTMENT MANAGEMENT ========================

    private static void handleAppointmentMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            displayAppointmentMenu();
            int choice = getIntInput("Enter your choice: ", scanner);
            
            switch (choice) {
                case 1 -> createAppointment(scanner);
                case 2 -> viewAllAppointments();
                case 3 -> viewAppointmentById(scanner);
                case 4 -> viewAppointmentsByPatient(scanner);
                case 5 -> viewAppointmentsByDoctor(scanner);
                case 6 -> viewAppointmentsByStatus(scanner);
                case 7 -> confirmAppointment(scanner);
                case 8 -> cancelAppointment(scanner);
                case 9 -> rescheduleAppointment(scanner);
                case 10 -> completeAppointment(scanner);
                case 11 -> viewUpcomingAppointments();
                case 12 -> demonstrateDeepCopy(scanner);
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayAppointmentMenu() {
        System.out.println("\n===========================================");
        System.out.println("        APPOINTMENT MANAGEMENT");
        System.out.println("===========================================");
        System.out.println("1. Create New Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("3. View Appointment by ID");
        System.out.println("4. View Appointments by Patient");
        System.out.println("5. View Appointments by Doctor");
        System.out.println("6. View Appointments by Status");
        System.out.println("7. Confirm Appointment");
        System.out.println("8. Cancel Appointment");
        System.out.println("9. Reschedule Appointment");
        System.out.println("10. Complete Appointment");
        System.out.println("11. View Upcoming Appointments");
        System.out.println("12. Demonstrate Deep Copy (Advanced Feature)");
        System.out.println("0. Back to Main Menu");
        System.out.println("===========================================");
    }

    private static void createAppointment(Scanner scanner) {
        System.out.println("\n--- Create New Appointment ---");
        
        try {
            System.out.println("\nAvailable Patients:");
            List<Patient> patients = patientService.getAllPatients();
            if (patients.isEmpty()) {
                System.out.println("No patients found. Please add patients first.");
                return;
            }
            patients.forEach(p -> System.out.println(p.getId() + ". " + p.getName()));
            Long patientId = getLongInput("Enter Patient ID: ", scanner);
            Optional<Patient> patientOpt = patientService.getPatientById(patientId);
            if (!patientOpt.isPresent()) {
                System.out.println("Patient not found.");
                return;
            }

            System.out.println("\nAvailable Doctors:");
            List<Doctor> doctors = doctorService.getAllDoctors();
            if (doctors.isEmpty()) {
                System.out.println("No doctors found. Please add doctors first.");
                return;
            }
            doctors.forEach(d -> System.out.println(d.getId() + ". Dr. " + d.getName() + 
                    " (" + d.getSpecialization().getDisplayName() + ")"));
            Long doctorId = getLongInput("Enter Doctor ID: ", scanner);
            Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
            if (!doctorOpt.isPresent()) {
                System.out.println("Doctor not found.");
                return;
            }

            System.out.print("Enter appointment date and time (yyyy-MM-dd HH:mm): ");
            String dateTimeStr = scanner.nextLine();
            LocalDateTime appointmentDateTime = LocalDateTime.parse(dateTimeStr, dateTimeFormatter);

            System.out.print("Enter symptoms: ");
            String symptoms = scanner.nextLine();

            System.out.print("Enter notes (optional): ");
            String notes = scanner.nextLine();

            Appointment appointment = appointmentService.createAppointment(
                    patientOpt.get(), doctorOpt.get(), appointmentDateTime, symptoms, notes
            );

            System.out.println("\n✓ Appointment created successfully!");
            System.out.println(appointment);

        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use yyyy-MM-dd HH:mm");
        } catch (InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error creating appointment: " + e.getMessage());
        }
    }

    private static void viewAllAppointments() {
        System.out.println("\n--- All Appointments ---");
        List<Appointment> appointments = appointmentService.getAllAppointments();
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        appointments.forEach(System.out::println);
        System.out.println("\nTotal Appointments: " + appointments.size());
    }

    private static void viewAppointmentById(Scanner scanner) {
        Long id = getLongInput("Enter Appointment ID: ", scanner);
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        
        if (appointment.isPresent()) {
            System.out.println("\n" + appointment.get());
        } else {
            System.out.println("Appointment not found.");
        }
    }

    private static void viewAppointmentsByPatient(Scanner scanner) {
        Long patientId = getLongInput("Enter Patient ID: ", scanner);
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments found for this patient.");
            return;
        }

        System.out.println("\n--- Appointments for Patient ID: " + patientId + " ---");
        appointments.forEach(System.out::println);
    }

    private static void viewAppointmentsByDoctor(Scanner scanner) {
        Long doctorId = getLongInput("Enter Doctor ID: ", scanner);
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments found for this doctor.");
            return;
        }

        System.out.println("\n--- Appointments for Doctor ID: " + doctorId + " ---");
        appointments.forEach(System.out::println);
    }

    private static void viewAppointmentsByStatus(Scanner scanner) {
        System.out.println("\nAvailable Statuses:");
        for (AppointmentStatus status : AppointmentStatus.values()) {
            System.out.println("- " + status.getDisplayName());
        }
        
        System.out.print("Enter status: ");
        String statusStr = scanner.nextLine().toUpperCase().replace(" ", "_");
        
        try {
            AppointmentStatus status = AppointmentStatus.valueOf(statusStr);
            List<Appointment> appointments = appointmentService.getAppointmentsByStatus(status);
            
            if (appointments.isEmpty()) {
                System.out.println("No appointments found with status: " + status.getDisplayName());
                return;
            }

            System.out.println("\n--- Appointments with Status: " + status.getDisplayName() + " ---");
            appointments.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status.");
        }
    }

    private static void confirmAppointment(Scanner scanner) {
        Long id = getLongInput("Enter Appointment ID to confirm: ", scanner);
        
        try {
            appointmentService.confirmAppointment(id);
            System.out.println("✓ Appointment confirmed successfully!");
        } catch (AppointmentNotFoundException | InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cancelAppointment(Scanner scanner) {
        Long id = getLongInput("Enter Appointment ID to cancel: ", scanner);
        System.out.print("Enter cancellation reason: ");
        String reason = scanner.nextLine();
        
        try {
            appointmentService.cancelAppointment(id, reason);
            System.out.println("✓ Appointment cancelled successfully!");
        } catch (AppointmentNotFoundException | InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void rescheduleAppointment(Scanner scanner) {
        Long id = getLongInput("Enter Appointment ID to reschedule: ", scanner);
        System.out.print("Enter new date and time (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.nextLine();
        
        try {
            LocalDateTime newDateTime = LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
            appointmentService.rescheduleAppointment(id, newDateTime);
            System.out.println("✓ Appointment rescheduled successfully!");
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format.");
        } catch (AppointmentNotFoundException | InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void completeAppointment(Scanner scanner) {
        Long id = getLongInput("Enter Appointment ID to mark as completed: ", scanner);
        
        try {
            appointmentService.completeAppointment(id);
            System.out.println("✓ Appointment marked as completed!");
        } catch (AppointmentNotFoundException | InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewUpcomingAppointments() {
        System.out.println("\n--- Upcoming Appointments ---");
        List<Appointment> appointments = appointmentService.getUpcomingAppointments();
        
        if (appointments.isEmpty()) {
            System.out.println("No upcoming appointments.");
            return;
        }

        appointments.forEach(System.out::println);
        System.out.println("\nTotal Upcoming: " + appointments.size());
    }

    private static void demonstrateDeepCopy(Scanner scanner) {
        System.out.println("\n===========================================");
        System.out.println("    DEEP COPY DEMONSTRATION");
        System.out.println("===========================================");
        
        Long id = getLongInput("Enter Appointment ID to demonstrate deep copy: ", scanner);
        Optional<Appointment> originalOpt = appointmentService.getAppointmentById(id);
        
        if (!originalOpt.isPresent()) {
            System.out.println("Appointment not found.");
            return;
        }

        Appointment original = originalOpt.get();
        System.out.println("\nOriginal Appointment:");
        System.out.println(original);
        System.out.println("Patient Name: " + original.getPatient().getName());

        Appointment cloned = original.clone();
        System.out.println("\n✓ Appointment cloned successfully!");
        System.out.println("\nCloned Appointment:");
        System.out.println(cloned);

        System.out.println("\n--- Testing Deep Copy ---");
        System.out.println("Modifying cloned patient's name...");
        String originalPatientName = original.getPatient().getName();
        cloned.getPatient().setName("Modified Clone Name");

        System.out.println("\nAfter Modification:");
        System.out.println("Original Patient Name: " + original.getPatient().getName());
        System.out.println("Cloned Patient Name: " + cloned.getPatient().getName());

        if (original.getPatient().getName().equals(originalPatientName)) {
            System.out.println("\n✓ DEEP COPY SUCCESSFUL!");
            System.out.println("Original object was NOT affected by changes to the clone.");
        } else {
            System.out.println("\n✗ SHALLOW COPY DETECTED!");
            System.out.println("Original object was affected by changes to the clone.");
        }
        
        System.out.println("===========================================");
    }

    // ======================== BILLING MANAGEMENT ========================


    private static void handleBillingMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            displayBillingMenu();
            int choice = getIntInput("Enter your choice: ", scanner);
            
            switch (choice) {
                case 1 -> generateBill(scanner);
                case 2 -> viewAllBills();
                case 3 -> viewBillById(scanner);
                case 4 -> viewBillByAppointment(scanner);
                case 5 -> viewBillsByPatient(scanner);
                case 6 -> viewUnpaidBills();
                case 7 -> markBillAsPaid(scanner);
                case 8 -> updateAdditionalCharges(scanner);
                case 9 -> viewBillSummary(scanner);
                case 10 -> viewRevenueStatistics();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayBillingMenu() {
        System.out.println("\n===========================================");
        System.out.println("          BILLING MANAGEMENT");
        System.out.println("===========================================");
        System.out.println("1. Generate Bill for Appointment");
        System.out.println("2. View All Bills");
        System.out.println("3. View Bill by ID");
        System.out.println("4. View Bill by Appointment");
        System.out.println("5. View Bills by Patient");
        System.out.println("6. View Unpaid Bills");
        System.out.println("7. Mark Bill as Paid");
        System.out.println("8. Update Additional Charges");
        System.out.println("9. View Bill Summary (Immutable)");
        System.out.println("10. View Revenue Statistics");
        System.out.println("0. Back to Main Menu");
        System.out.println("===========================================");
    }

    private static void generateBill(Scanner scanner) {
        System.out.println("\n--- Generate Bill ---");
        
        Long appointmentId = getLongInput("Enter Appointment ID: ", scanner);
        Optional<Appointment> appointmentOpt = appointmentService.getAppointmentById(appointmentId);
        
        if (!appointmentOpt.isPresent()) {
            System.out.println("Appointment not found.");
            return;
        }

        try {
            double additionalCharges = getDoubleInput("Enter additional charges (0 if none): ", scanner);
            double taxRate = getDoubleInput("Enter tax rate (default 18%): ", scanner);
            
            if (taxRate == 0) {
                taxRate = 18.0;
            }

            Bill bill = billService.generateBill(appointmentOpt.get(), additionalCharges, taxRate);
            System.out.println("\n✓ Bill generated successfully!");
            System.out.println(bill);
            System.out.println("\nBill Details:");
            System.out.println("Consultation Fee: ₹" + bill.getConsultationFee());
            System.out.println("Additional Charges: ₹" + bill.getAdditionalCharges());
            System.out.println("Subtotal: ₹" + bill.getSubtotal());
            System.out.println("Tax (" + bill.getTaxRate() + "%): ₹" + bill.getTaxAmount());
            System.out.println("Total Amount: ₹" + bill.getTotalAmount());

        } catch (InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllBills() {
        System.out.println("\n--- All Bills ---");
        List<Bill> bills = billService.getAllBills();
        
        if (bills.isEmpty()) {
            System.out.println("No bills found.");
            return;
        }

        bills.forEach(System.out::println);
        System.out.println("\nTotal Bills: " + bills.size());
    }

    private static void viewBillById(Scanner scanner) {
        Long id = getLongInput("Enter Bill ID: ", scanner);
        Optional<Bill> bill = billService.getBillById(id);
        
        if (bill.isPresent()) {
            displayBillDetails(bill.get());
        } else {
            System.out.println("Bill not found.");
        }
    }

    private static void viewBillByAppointment(Scanner scanner) {
        Long appointmentId = getLongInput("Enter Appointment ID: ", scanner);
        Optional<Bill> bill = billService.getBillByAppointment(appointmentId);
        
        if (bill.isPresent()) {
            displayBillDetails(bill.get());
        } else {
            System.out.println("No bill found for this appointment.");
        }
    }

    private static void viewBillsByPatient(Scanner scanner) {
        Long patientId = getLongInput("Enter Patient ID: ", scanner);
        List<Bill> bills = billService.getBillsByPatient(patientId);
        
        if (bills.isEmpty()) {
            System.out.println("No bills found for this patient.");
            return;
        }

        System.out.println("\n--- Bills for Patient ID: " + patientId + " ---");
        bills.forEach(System.out::println);
        
        double totalAmount = bills.stream().mapToDouble(Bill::getTotalAmount).sum();
        double paidAmount = bills.stream().filter(Bill::isPaid).mapToDouble(Bill::getTotalAmount).sum();
        double unpaidAmount = totalAmount - paidAmount;
        
        System.out.println("\n--- Patient Bill Summary ---");
        System.out.println("Total Bills: " + bills.size());
        System.out.println("Total Amount: ₹" + String.format("%.2f", totalAmount));
        System.out.println("Paid Amount: ₹" + String.format("%.2f", paidAmount));
        System.out.println("Unpaid Amount: ₹" + String.format("%.2f", unpaidAmount));
    }

    private static void viewUnpaidBills() {
        System.out.println("\n--- Unpaid Bills ---");
        List<Bill> bills = billService.getUnpaidBills();
        
        if (bills.isEmpty()) {
            System.out.println("No unpaid bills.");
            return;
        }

        bills.forEach(System.out::println);
        double totalUnpaid = bills.stream().mapToDouble(Bill::getTotalAmount).sum();
        System.out.println("\nTotal Unpaid Amount: ₹" + String.format("%.2f", totalUnpaid));
    }

    private static void markBillAsPaid(Scanner scanner) {
        Long id = getLongInput("Enter Bill ID to mark as paid: ", scanner);
        System.out.print("Enter payment method (Cash/Card/UPI): ");
        String paymentMethod = scanner.nextLine();
        
        try {
            billService.markBillAsPaid(id, paymentMethod);
            System.out.println("✓ Bill marked as paid successfully!");
        } catch (InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateAdditionalCharges(Scanner scanner) {
        Long id = getLongInput("Enter Bill ID: ", scanner);
        double additionalCharges = getDoubleInput("Enter new additional charges: ", scanner);
        
        try {
            billService.updateAdditionalCharges(id, additionalCharges);
            System.out.println("✓ Additional charges updated successfully!");
            
            Optional<Bill> bill = billService.getBillById(id);
            if (bill.isPresent()) {
                System.out.println("New Total Amount: ₹" + bill.get().getTotalAmount());
            }
        } catch (InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void viewBillSummary(Scanner scanner) {
        System.out.println("\n===========================================");
        System.out.println("    BILL SUMMARY (Immutable Class Demo)");
        System.out.println("===========================================");
        
        Long id = getLongInput("Enter Bill ID: ", scanner);
        
        try {
            BillSummary summary = billService.generateBillSummary(id);
            System.out.println("\n" + summary.getFormattedSummary());
            
            System.out.println("\n--- Immutability Demonstration ---");
            System.out.println("This BillSummary object is immutable:");
            System.out.println("- All fields are final");
            System.out.println("- No setter methods exist");
            System.out.println("- Thread-safe and cacheable");
            System.out.println("- Once created, values cannot be changed");
            
        } catch (InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewRevenueStatistics() {
        System.out.println("\n===========================================");
        System.out.println("         REVENUE STATISTICS");
        System.out.println("===========================================");
        
        double totalRevenue = billService.calculateTotalRevenue();
        double outstandingAmount = billService.calculateOutstandingAmount();
        int totalBills = billService.getBillCount();
        int paidBills = billService.getPaidBills().size();
        int unpaidBills = billService.getUnpaidBills().size();
        
        System.out.println("Total Bills: " + totalBills);
        System.out.println("Paid Bills: " + paidBills);
        System.out.println("Unpaid Bills: " + unpaidBills);
        System.out.println("-------------------------------------------");
        System.out.println("Total Revenue (Paid): ₹" + String.format("%.2f", totalRevenue));
        System.out.println("Outstanding Amount: ₹" + String.format("%.2f", outstandingAmount));
        System.out.println("Total Expected: ₹" + String.format("%.2f", totalRevenue + outstandingAmount));
        System.out.println("===========================================");
    }

    private static void displayBillDetails(Bill bill) {
        System.out.println("\n===========================================");
        System.out.println("             BILL DETAILS");
        System.out.println("===========================================");
        System.out.println("Bill ID: " + bill.getId());
        System.out.println("Appointment ID: " + bill.getAppointment().getId());
        System.out.println("Patient: " + bill.getAppointment().getPatient().getName());
        System.out.println("Doctor: Dr. " + bill.getAppointment().getDoctor().getName());
        System.out.println("-------------------------------------------");
        System.out.println("Consultation Fee: ₹" + String.format("%.2f", bill.getConsultationFee()));
        System.out.println("Additional Charges: ₹" + String.format("%.2f", bill.getAdditionalCharges()));
        System.out.println("Subtotal: ₹" + String.format("%.2f", bill.getSubtotal()));
        System.out.println("Tax (" + bill.getTaxRate() + "%): ₹" + String.format("%.2f", bill.getTaxAmount()));
        System.out.println("-------------------------------------------");
        System.out.println("Total Amount: ₹" + String.format("%.2f", bill.getTotalAmount()));
        System.out.println("Payment Status: " + (bill.isPaid() ? "PAID" : "UNPAID"));
        if (bill.isPaid()) {
            System.out.println("Payment Method: " + bill.getPaymentMethod());
            System.out.println("Payment Date: " + bill.getPaymentDateTime());
        }
        System.out.println("===========================================");
    }

    // ======================== UTILITY METHODS ========================

    private static int getIntInput(String prompt, Scanner scanner) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static Long getLongInput(String prompt, Scanner scanner) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.valueOf(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid ID.");
            }
        }
    }

    private static double getDoubleInput(String prompt, Scanner scanner) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}