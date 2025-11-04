# API Testing Examples for Admin Module

This file contains example requests for testing the Admin Module APIs using tools like Postman, cURL, or REST Client.

## Prerequisites
- Application should be running on `http://localhost:8080`
- Database should have some test data

## 1. Appointment Management APIs

### 1.1 Get All Pending Appointments
**Request:**
```http
GET http://localhost:8080/api/admin/appointments/pending
Accept: application/json
```

**Expected Response (200 OK):**
```json
[
  {
    "appointmentId": "APT001",
    "customerId": "CUST001",
    "vehicleId": "VEH001",
    "description": "Regular service check",
    "appointmentDateTime": "2025-11-10T10:00:00",
    "status": "PENDING",
    "assignedEmployeeId": null,
    "assignedEmployeeName": null,
    "createdAt": "2025-11-04T09:00:00",
    "updatedAt": "2025-11-04T09:00:00"
  }
]
```

### 1.2 Get All Appointments
**Request:**
```http
GET http://localhost:8080/api/admin/appointments
Accept: application/json
```

### 1.3 Get Appointment by ID
**Request:**
```http
GET http://localhost:8080/api/admin/appointments/APT001
Accept: application/json
```

### 1.4 Assign Appointment to Employee
**Request:**
```http
PUT http://localhost:8080/api/admin/appointments/APT001/assign/EMP001
Accept: application/json
```

**Expected Response (200 OK):**
```json
{
  "appointmentId": "APT001",
  "customerId": "CUST001",
  "vehicleId": "VEH001",
  "description": "Regular service check",
  "appointmentDateTime": "2025-11-10T10:00:00",
  "status": "ASSIGNED",
  "assignedEmployeeId": "EMP001",
  "assignedEmployeeName": "John Doe",
  "createdAt": "2025-11-04T09:00:00",
  "updatedAt": "2025-11-04T14:30:00"
}
```

**Error Response (400 BAD REQUEST):**
```json
{
  "message": "Employee is not available for assignment"
}
```

## 2. Project Management APIs

### 2.1 Get All Pending Projects
**Request:**
```http
GET http://localhost:8080/api/admin/projects/pending
Accept: application/json
```

**Expected Response (200 OK):**
```json
[
  {
    "projectId": "PRJ001",
    "customerId": "CUST001",
    "vehicleId": "VEH001",
    "projectName": "Engine Overhaul",
    "description": "Complete engine overhaul and maintenance",
    "startDate": "2025-11-15T08:00:00",
    "expectedEndDate": "2025-11-20T17:00:00",
    "status": "PENDING",
    "assignedEmployeeId": null,
    "assignedEmployeeName": null,
    "createdAt": "2025-11-04T10:00:00",
    "updatedAt": "2025-11-04T10:00:00"
  }
]
```

### 2.2 Get All Projects
**Request:**
```http
GET http://localhost:8080/api/admin/projects
Accept: application/json
```

### 2.3 Get Project by ID
**Request:**
```http
GET http://localhost:8080/api/admin/projects/PRJ001
Accept: application/json
```

### 2.4 Assign Project to Employee
**Request:**
```http
PUT http://localhost:8080/api/admin/projects/PRJ001/assign/EMP002
Accept: application/json
```

**Expected Response (200 OK):**
```json
{
  "projectId": "PRJ001",
  "customerId": "CUST001",
  "vehicleId": "VEH001",
  "projectName": "Engine Overhaul",
  "description": "Complete engine overhaul and maintenance",
  "startDate": "2025-11-15T08:00:00",
  "expectedEndDate": "2025-11-20T17:00:00",
  "status": "ASSIGNED",
  "assignedEmployeeId": "EMP002",
  "assignedEmployeeName": "Jane Smith",
  "createdAt": "2025-11-04T10:00:00",
  "updatedAt": "2025-11-04T14:45:00"
}
```

## 3. Employee Management APIs

### 3.1 Get All Available Employees
**Request:**
```http
GET http://localhost:8080/api/admin/employees/available
Accept: application/json
```

**Expected Response (200 OK):**
```json
[
  {
    "employeeId": "EMP001",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "specialization": "Engine Specialist",
    "available": true,
    "createdAt": "2025-11-01T08:00:00",
    "updatedAt": "2025-11-01T08:00:00"
  },
  {
    "employeeId": "EMP002",
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phone": "+1234567891",
    "specialization": "Electrical Systems",
    "available": true,
    "createdAt": "2025-11-01T08:00:00",
    "updatedAt": "2025-11-01T08:00:00"
  }
]
```

### 3.2 Get All Employees
**Request:**
```http
GET http://localhost:8080/api/admin/employees
Accept: application/json
```

### 3.3 Get Employee by ID
**Request:**
```http
GET http://localhost:8080/api/admin/employees/EMP001
Accept: application/json
```

**Expected Response (200 OK):**
```json
{
  "employeeId": "EMP001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "specialization": "Engine Specialist",
  "available": true,
  "createdAt": "2025-11-01T08:00:00",
  "updatedAt": "2025-11-01T08:00:00"
}
```

**Error Response (404 NOT FOUND):**
```json
{
  "message": "Employee not found with id: EMP999"
}
```

## cURL Commands

### Get Pending Appointments
```bash
curl -X GET http://localhost:8080/api/admin/appointments/pending -H "Accept: application/json"
```

### Get Available Employees
```bash
curl -X GET http://localhost:8080/api/admin/employees/available -H "Accept: application/json"
```

### Assign Appointment to Employee
```bash
curl -X PUT http://localhost:8080/api/admin/appointments/APT001/assign/EMP001 -H "Accept: application/json"
```

### Get Pending Projects
```bash
curl -X GET http://localhost:8080/api/admin/projects/pending -H "Accept: application/json"
```

### Assign Project to Employee
```bash
curl -X PUT http://localhost:8080/api/admin/projects/PRJ001/assign/EMP002 -H "Accept: application/json"
```

## PowerShell Commands

### Get Pending Appointments
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/appointments/pending" -Method Get -Headers @{"Accept"="application/json"}
```

### Get Available Employees
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/employees/available" -Method Get -Headers @{"Accept"="application/json"}
```

### Assign Appointment to Employee
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/appointments/APT001/assign/EMP001" -Method Put -Headers @{"Accept"="application/json"}
```

## Testing Workflow

1. **Setup Test Data**: Insert test data for customers, vehicles, employees, appointments, and projects
2. **Test Employee Listing**: Verify you can retrieve available employees
3. **Test Pending Items**: Verify you can retrieve pending appointments and projects
4. **Test Assignment**: Try assigning an appointment/project to an employee
5. **Test Validation**: Try assigning to non-existent or unavailable employee (should fail)
6. **Test Status Change**: Verify status changes from PENDING to ASSIGNED after assignment

## Sample Test Data SQL

```sql
-- Insert test employees
INSERT INTO Employees (employeeId, name, email, phone, specialization, isAvailable, createdAt, updatedAt)
VALUES 
('EMP001', 'John Doe', 'john.doe@example.com', '+1234567890', 'Engine Specialist', true, NOW(), NOW()),
('EMP002', 'Jane Smith', 'jane.smith@example.com', '+1234567891', 'Electrical Systems', true, NOW(), NOW()),
('EMP003', 'Bob Wilson', 'bob.wilson@example.com', '+1234567892', 'Body Work', false, NOW(), NOW());

-- Insert test appointments
INSERT INTO Appointments (appointmentId, customerId, vehicleId, description, appointmentDateTime, status, createdAt, updatedAt)
VALUES 
('APT001', 'CUST001', 'VEH001', 'Regular service check', '2025-11-10 10:00:00', 'PENDING', NOW(), NOW()),
('APT002', 'CUST002', 'VEH002', 'Oil change', '2025-11-11 14:00:00', 'PENDING', NOW(), NOW());

-- Insert test projects
INSERT INTO Projects (projectId, customerId, vehicleId, projectName, description, startDate, expectedEndDate, status, createdAt, updatedAt)
VALUES 
('PRJ001', 'CUST001', 'VEH001', 'Engine Overhaul', 'Complete engine overhaul and maintenance', '2025-11-15 08:00:00', '2025-11-20 17:00:00', 'PENDING', NOW(), NOW()),
('PRJ002', 'CUST003', 'VEH003', 'Paint Job', 'Full body paint and detailing', '2025-11-12 09:00:00', '2025-11-14 16:00:00', 'PENDING', NOW(), NOW());
```
