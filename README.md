# MediTrack System

MediTrack is a console-based application designed to manage patient, doctor, appointment, and billing information for a medical practice. It provides functionalities for scheduling, updating, and canceling appointments, generating and managing bills, and viewing various statistics.

## Features

*   **Patient Management**: (Placeholder - Not fully implemented in `Main.java` but services exist)
*   **Doctor Management**: (Placeholder - Not fully implemented in `Main.java` but services exist)
*   **Appointment Management**:
    *   Create, view, confirm, cancel, reschedule, and complete appointments.
    *   View appointments by ID, patient, doctor, and status.
    *   View upcoming appointments.
    *   Demonstrates deep copy functionality for appointments.
*   **Billing Management**:
    *   Generate bills for appointments, including additional charges and tax.
    *   View all bills, bills by ID, by appointment, and by patient.
    *   Manage unpaid bills and mark bills as paid.
    *   Update additional charges on existing bills.
    *   Generate immutable bill summaries.
    *   View revenue statistics (total revenue, outstanding amount).
*   **Data Validation & Exception Handling**: Robust input validation and custom exceptions to ensure data integrity.

## Technologies Used

*   **Java**: Core programming language.
*   **Gradle**: Build automation tool.
*   **JUnit 5**: Testing framework.

## Project Structure

```
.
├── build.gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── airtribe/
    │   │           └── meditrack/
    │   │               ├── Main.java             // Main application entry point
    │   │               ├── entity/               // Data model classes (Doctor, Patient, Appointment, Bill, etc.)
    │   │               ├── exceptions/           // Custom exception classes
    │   │               ├── interfaces/           // Interface definitions (Payable, Searchable)
    │   │               ├── service/              // Business logic services (AppointmentService, BillService, etc.)
    │   │               └── util/                 // Utility classes (DataStore, IdGenerator, Validator)
    └── test/
        └── java/
```

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd meditrack
    ```
2.  **Build the project using Gradle:**
    ```bash
    ./gradlew build
    ```

## How to Run

After building the project, you can run the application from the command line:

```bash
./gradlew run
```

This will start the console application, presenting you with the main menu to interact with the MediTrack system.

## Usage

Follow the on-screen menu prompts to navigate through Patient, Doctor, Appointment, and Billing management options.

## Testing

To run the unit tests, use the following Gradle command:

```bash
./gradlew test
```
