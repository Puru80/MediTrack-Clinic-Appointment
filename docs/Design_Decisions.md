# Design Decisions

This document highlights the key design decisions made during the development of the MediTrack system.

## 1. Console-Based User Interface

The application is built as a console-based program. This decision was made for simplicity and to focus on the core business logic of the application without the overhead of a graphical user interface (GUI). The main interaction is handled through a menu-driven system in the `Main` class.

## 2. Service-Oriented Architecture

The application's business logic is encapsulated within a service layer. Each major entity (e.g., `Patient`, `Doctor`, `Appointment`, `Bill`) has a corresponding service class (e.g., `PatientService`, `DoctorService`). This separation of concerns makes the code more modular, easier to maintain, and easier to test.

## 3. In-Memory Data Store

For simplicity, the application uses an in-memory data store (`DataStore` class). This class uses a `HashMap` to store and manage the application's data. This approach is suitable for a prototype or a small-scale application where data persistence is not a primary requirement. For a production system, this could be replaced with a database (e.g., H2, PostgreSQL) without significantly altering the service layer.

## 4. Singleton ID Generator

The `IdGenerator` class is implemented as a singleton. This ensures that a single instance of the ID generator is used throughout the application, guaranteeing that all generated IDs for patients, doctors, appointments, and bills are unique.

## 5. Custom Exception Handling

The application uses custom exceptions (e.g., `InvalidDataException`, `AppointmentNotFoundException`) to handle specific error conditions. This allows for more specific error handling and makes the code more readable and maintainable.

## 6. Immutable BillSummary Class

The `BillSummary` class is designed to be immutable. This means that once an instance of `BillSummary` is created, its state cannot be changed. This is a good practice for data transfer objects (DTOs) and for ensuring thread safety.

## 7. Deep Copy for Appointments

The `Appointment` class implements a deep copy `clone()` method. This ensures that when an `Appointment` object is cloned, all its internal objects (like `Patient` and `Doctor`) are also cloned, creating a completely independent copy. This is useful for scenarios where you want to modify a copy of an object without affecting the original.

## 8. Manual Test Runner

A `TestRunner` class is provided for manual, end-to-end testing of the application's services. This allows for quick verification of functionalities without the need to write extensive JUnit tests for every scenario. It is not part of the main application and is intended for development and testing purposes only.

## 9. Use of Enums for Specializations and Statuses

The application uses enums for `Specialization` and `AppointmentStatus`. This provides type safety and makes the code more readable and less prone to errors compared to using plain strings or integers.
