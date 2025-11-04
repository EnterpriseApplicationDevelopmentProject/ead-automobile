# Admin Module Implementation Guide

## Overview
This document describes the admin functionality implementation for the EAD Automobile System. The admin module allows administrators to view appointments and projects created by customers and assign them to available employees.

## Implementation Summary

### 1. Entity Models Updated

#### Appointment Entity
- **Location**: `src/main/java/com/example/ead_backend/model/entity/Appointment.java`
- **Fields Added**:
  - `customerId`: Reference to the customer who created the appointment
  - `vehicleId`: Vehicle for which appointment is created
  - `description`: Appointment details
  - `appointmentDateTime`: Scheduled date and time
  - `status`: Current status (PENDING, ASSIGNED, CONFIRMED, etc.)
  - `assignedEmployeeId`: Employee assigned to handle the appointment
  - `createdAt`, `updatedAt`: Timestamps
  - `assignedEmployee`: ManyToOne relationship with Employee

#### Project Entity
- **Location**: `src/main/java/com/example/ead_backend/model/entity/Project.java`
- **Fields Added**:
  - `customerId`: Reference to the customer who created the project
  - `vehicleId`: Vehicle for the project
  - `projectName`: Name of the project
  - `description`: Project details
  - `startDate`, `expectedEndDate`: Project timeline
  - `status`: Current status (PENDING, ASSIGNED, IN_PROGRESS, etc.)
  - `assignedEmployeeId`: Employee assigned to handle the project
  - `createdAt`, `updatedAt`: Timestamps
  - `assignedEmployee`: ManyToOne relationship with Employee

#### Employee Entity
- **Location**: `src/main/java/com/example/ead_backend/model/entity/Employee.java`
- **Fields Added**:
  - `name`, `email`, `phone`: Employee contact information
  - `specialization`: Employee's area of expertise
  - `isAvailable`: Availability status for new assignments
  - `createdAt`, `updatedAt`: Timestamps
  - `appointments`, `projects`: OneToMany relationships

### 2. Enums

#### AppointmentStatus
- **Location**: `src/main/java/com/example/ead_backend/model/enums/AppointmentStatus.java`
- **Values**:
  - `PENDING`: Created by customer, waiting for admin assignment
  - `ASSIGNED`: Assigned to an employee by admin
  - `CONFIRMED`: Employee confirmed the appointment
  - `IN_PROGRESS`: Appointment in progress
  - `COMPLETED`: Appointment completed
  - `CANCELLED`: Appointment cancelled
  - `NO_SHOW`: Customer didn't show up

#### ProjectStatus
- **Location**: `src/main/java/com/example/ead_backend/model/enums/ProjectStatus.java`
- **Values**:
  - `PENDING`: Created by customer, waiting for admin assignment
  - `ASSIGNED`: Assigned to an employee by admin
  - `IN_PROGRESS`: Project work in progress
  - `COMPLETED`: Project completed
  - `CANCELLED`: Project cancelled
  - `ON_HOLD`: Project temporarily on hold

### 3. DTOs (Data Transfer Objects)

#### AppointmentDTO
- **Location**: `src/main/java/com/example/ead_backend/dto/AppointmentDTO.java`
- Used for transferring appointment data between layers

#### ProjectDTO
- **Location**: `src/main/java/com/example/ead_backend/dto/ProjectDTO.java`
- Used for transferring project data between layers

#### EmployeeDTO
- **Location**: `src/main/java/com/example/ead_backend/dto/EmployeeDTO.java`
- Used for transferring employee data between layers

### 4. Repositories

#### AppointmentRepository
- **Location**: `src/main/java/com/example/ead_backend/repository/AppointmentRepository.java`
- **Custom Methods**:
  - `findByStatus(AppointmentStatus status)`: Find appointments by status
  - `findByAssignedEmployeeId(String employeeId)`: Find appointments assigned to an employee
  - `findAllPendingAppointments()`: Find all pending appointments

#### ProjectRepository
- **Location**: `src/main/java/com/example/ead_backend/repository/ProjectRepository.java`
- **Custom Methods**:
  - `findByStatus(ProjectStatus status)`: Find projects by status
  - `findByAssignedEmployeeId(String employeeId)`: Find projects assigned to an employee
  - `findAllPendingProjects()`: Find all pending projects

#### EmployeeRepository
- **Location**: `src/main/java/com/example/ead_backend/repository/EmployeeRepository.java`
- **Custom Methods**:
  - `findByIsAvailable(boolean isAvailable)`: Find available/unavailable employees
  - `findBySpecializationAndIsAvailable(String specialization, boolean isAvailable)`: Find available employees by specialization
  - `findAllAvailableEmployees()`: Find all available employees

### 5. Service Layer

#### AdminService Interface
- **Location**: `src/main/java/com/example/ead_backend/service/AdminService.java`
- **Methods**:
  - Appointment Management:
    - `getAllPendingAppointments()`: Get all unassigned appointments
    - `getAllAppointments()`: Get all appointments
    - `getAppointmentById(String appointmentId)`: Get specific appointment
    - `assignAppointmentToEmployee(String appointmentId, String employeeId)`: Assign appointment
  - Project Management:
    - `getAllPendingProjects()`: Get all unassigned projects
    - `getAllProjects()`: Get all projects
    - `getProjectById(String projectId)`: Get specific project
    - `assignProjectToEmployee(String projectId, String employeeId)`: Assign project
  - Employee Management:
    - `getAllAvailableEmployees()`: Get all available employees
    - `getAllEmployees()`: Get all employees
    - `getEmployeeById(String employeeId)`: Get specific employee

#### AdminServiceImpl
- **Location**: `src/main/java/com/example/ead_backend/service/impl/AdminServiceImpl.java`
- Implements all AdminService interface methods
- Includes validation logic (e.g., checking employee availability)
- Includes DTO conversion methods
- Updates status when assigning appointments/projects

### 6. Controller Layer

#### AdminController
- **Location**: `src/main/java/com/example/ead_backend/controller/AdminController.java`
- **Base URL**: `/api/admin`
- **Endpoints**:

##### Appointment Endpoints
- `GET /api/admin/appointments/pending`: Get all pending appointments
- `GET /api/admin/appointments`: Get all appointments
- `GET /api/admin/appointments/{appointmentId}`: Get appointment by ID
- `PUT /api/admin/appointments/{appointmentId}/assign/{employeeId}`: Assign appointment to employee

##### Project Endpoints
- `GET /api/admin/projects/pending`: Get all pending projects
- `GET /api/admin/projects`: Get all projects
- `GET /api/admin/projects/{projectId}`: Get project by ID
- `PUT /api/admin/projects/{projectId}/assign/{employeeId}`: Assign project to employee

##### Employee Endpoints
- `GET /api/admin/employees/available`: Get all available employees
- `GET /api/admin/employees`: Get all employees
- `GET /api/admin/employees/{employeeId}`: Get employee by ID

## API Usage Examples

### 1. Get All Pending Appointments
```http
GET http://localhost:8080/api/admin/appointments/pending
```

### 2. Get All Available Employees
```http
GET http://localhost:8080/api/admin/employees/available
```

### 3. Assign Appointment to Employee
```http
PUT http://localhost:8080/api/admin/appointments/APT001/assign/EMP001
```

### 4. Get All Pending Projects
```http
GET http://localhost:8080/api/admin/projects/pending
```

### 5. Assign Project to Employee
```http
PUT http://localhost:8080/api/admin/projects/PRJ001/assign/EMP001
```

## Workflow

1. **Customer Creates Appointment/Project**: 
   - Customer creates an appointment or project through their interface (implemented by your team member)
   - Status is set to `PENDING`
   - `assignedEmployeeId` is `null`

2. **Admin Views Pending Items**:
   - Admin accesses `/api/admin/appointments/pending` or `/api/admin/projects/pending`
   - System returns all appointments/projects with `PENDING` status

3. **Admin Checks Available Employees**:
   - Admin accesses `/api/admin/employees/available`
   - System returns employees where `isAvailable = true`

4. **Admin Assigns to Employee**:
   - Admin calls assignment endpoint with appointmentId/projectId and employeeId
   - System validates:
     - Appointment/Project exists
     - Employee exists
     - Employee is available
   - Updates status to `ASSIGNED`
   - Sets `assignedEmployeeId`
   - Updates `updatedAt` timestamp

5. **Employee Handles Assignment**:
   - Employee can view their assigned appointments/projects
   - Employee updates status as work progresses

## Error Handling

The implementation includes proper error handling:
- `404 NOT_FOUND`: When appointment/project/employee doesn't exist
- `400 BAD_REQUEST`: When trying to assign to unavailable employee or invalid data
- `500 INTERNAL_SERVER_ERROR`: For unexpected errors

## Notes for Integration

1. **Your team member's part** (Customer creating appointments/projects) should:
   - Set `status = AppointmentStatus.PENDING` or `ProjectStatus.PENDING`
   - Leave `assignedEmployeeId` as `null`
   - Set `createdAt` to current timestamp

2. **Database Configuration**: 
   - Ensure your `application.properties` has correct database settings
   - Run the application to auto-create tables (if using `spring.jpa.hibernate.ddl-auto=update`)

3. **Testing**: 
   - Test with sample data
   - Ensure appointments/projects are created with PENDING status
   - Verify admin can fetch and assign them

## Future Enhancements

Consider implementing:
1. Email notifications to employees when assigned
2. Workload calculation for employees
3. Auto-assignment based on availability and specialization
4. Dashboard with statistics
5. Reassignment functionality
6. Appointment/Project history tracking
