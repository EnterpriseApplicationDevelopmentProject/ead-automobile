# Admin CRUD Operations - Complete API Guide

## Overview
This document provides complete CRUD (Create, Read, Update, Delete) operations for the admin module.

## Base URL
```
http://localhost:8080/api/admin
```

---

## 📋 Appointment CRUD Operations

### 1. Get All Pending Appointments
```http
GET /api/admin/appointments/pending
```
**Response**: List of pending appointments (status = PENDING)

### 2. Get All Appointments
```http
GET /api/admin/appointments
```
**Response**: List of all appointments

### 3. Get Appointment by ID
```http
GET /api/admin/appointments/{appointmentId}
```
**Response**: Single appointment details

### 4. Create Appointment
```http
POST /api/admin/appointments
Content-Type: application/json

{
  "customerId": "CUST001",
  "vehicleId": "VEH001",
  "description": "Oil change and tire rotation",
  "appointmentDateTime": "2025-11-15T10:00:00",
  "status": "PENDING"
}
```
**Response**: Created appointment with generated ID

### 5. Update Appointment
```http
PUT /api/admin/appointments/{appointmentId}
Content-Type: application/json

{
  "description": "Updated description",
  "appointmentDateTime": "2025-11-16T10:00:00",
  "status": "CONFIRMED"
}
```
**Response**: Updated appointment details

### 6. Delete Appointment
```http
DELETE /api/admin/appointments/{appointmentId}
```
**Response**: 204 No Content

### 7. Assign Appointment to Employee
```http
PUT /api/admin/appointments/{appointmentId}/assign/{employeeId}
```
**Response**: Appointment with status changed to ASSIGNED

---

## 🚀 Project CRUD Operations

### 1. Get All Pending Projects
```http
GET /api/admin/projects/pending
```
**Response**: List of pending projects (status = PENDING)

### 2. Get All Projects
```http
GET /api/admin/projects
```
**Response**: List of all projects

### 3. Get Project by ID
```http
GET /api/admin/projects/{projectId}
```
**Response**: Single project details

### 4. Create Project
```http
POST /api/admin/projects
Content-Type: application/json

{
  "customerId": "CUST001",
  "vehicleId": "VEH001",
  "projectName": "Engine Overhaul",
  "description": "Complete engine rebuild",
  "startDate": "2025-11-20T08:00:00",
  "expectedEndDate": "2025-11-27T17:00:00",
  "status": "PENDING"
}
```
**Response**: Created project with generated ID

### 5. Update Project
```http
PUT /api/admin/projects/{projectId}
Content-Type: application/json

{
  "projectName": "Updated Engine Overhaul",
  "description": "Updated description",
  "expectedEndDate": "2025-11-28T17:00:00",
  "status": "IN_PROGRESS"
}
```
**Response**: Updated project details

### 6. Delete Project
```http
DELETE /api/admin/projects/{projectId}
```
**Response**: 204 No Content

### 7. Assign Project to Employee
```http
PUT /api/admin/projects/{projectId}/assign/{employeeId}
```
**Response**: Project with status changed to ASSIGNED

---

## ✅ Task CRUD Operations

### 1. Get All Tasks for Appointment
```http
GET /api/admin/appointments/{appointmentId}/tasks
```
**Response**: List of all tasks under the appointment

### 2. Get Task by ID
```http
GET /api/admin/tasks/{taskId}
```
**Response**: Single task details

### 3. Create Task
```http
POST /api/admin/tasks
Content-Type: application/json

{
  "appointmentId": "APT001",
  "taskName": "Replace oil filter",
  "description": "Remove old filter and install new one",
  "assignedEmployeeId": "EMP001",
  "status": "TODO",
  "dueDate": "2025-11-15T12:00:00"
}
```
**Response**: Created task with generated ID

### 4. Update Task
```http
PUT /api/admin/tasks/{taskId}
Content-Type: application/json

{
  "taskName": "Updated task name",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "dueDate": "2025-11-16T12:00:00"
}
```
**Response**: Updated task details

### 5. Delete Task
```http
DELETE /api/admin/tasks/{taskId}
```
**Response**: 204 No Content

---

## 👥 Employee CRUD Operations

### 1. Get All Available Employees
```http
GET /api/admin/employees/available
```
**Response**: List of employees where isAvailable = true

### 2. Get All Employees
```http
GET /api/admin/employees
```
**Response**: List of all employees

### 3. Get Employee by ID
```http
GET /api/admin/employees/{employeeId}
```
**Response**: Single employee details

### 4. Create Employee
```http
POST /api/admin/employees
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "specialization": "Engine Specialist",
  "isAvailable": true
}
```
**Response**: Created employee with generated ID

### 5. Update Employee
```http
PUT /api/admin/employees/{employeeId}
Content-Type: application/json

{
  "name": "John Doe Updated",
  "email": "john.updated@example.com",
  "phone": "+1234567899",
  "specialization": "Senior Engine Specialist",
  "isAvailable": false
}
```
**Response**: Updated employee details

### 6. Delete Employee
```http
DELETE /api/admin/employees/{employeeId}
```
**Response**: 204 No Content

---

## PowerShell Testing Examples

### Appointment Operations
```powershell
# Create Appointment
$appointmentBody = @{
    customerId = "CUST001"
    vehicleId = "VEH001"
    description = "Oil change"
    appointmentDateTime = "2025-11-15T10:00:00"
    status = "PENDING"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/admin/appointments" `
    -Method Post `
    -Body $appointmentBody `
    -ContentType "application/json"

# Update Appointment
$updateBody = @{
    description = "Oil change and tire rotation"
    status = "CONFIRMED"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/admin/appointments/APT001" `
    -Method Put `
    -Body $updateBody `
    -ContentType "application/json"

# Delete Appointment
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/appointments/APT001" `
    -Method Delete
```

### Project Operations
```powershell
# Create Project
$projectBody = @{
    customerId = "CUST001"
    vehicleId = "VEH001"
    projectName = "Engine Overhaul"
    description = "Complete engine rebuild"
    startDate = "2025-11-20T08:00:00"
    expectedEndDate = "2025-11-27T17:00:00"
    status = "PENDING"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/admin/projects" `
    -Method Post `
    -Body $projectBody `
    -ContentType "application/json"

# Update Project
$updateProjectBody = @{
    projectName = "Updated Engine Overhaul"
    status = "IN_PROGRESS"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/admin/projects/PRJ001" `
    -Method Put `
    -Body $updateProjectBody `
    -ContentType "application/json"

# Delete Project
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/projects/PRJ001" `
    -Method Delete
```

### Task Operations
```powershell
# Create Task
$taskBody = @{
    appointmentId = "APT001"
    taskName = "Replace oil filter"
    description = "Remove old and install new"
    assignedEmployeeId = "EMP001"
    status = "TODO"
    dueDate = "2025-11-15T12:00:00"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/admin/tasks" `
    -Method Post `
    -Body $taskBody `
    -ContentType "application/json"

# Get Tasks for Appointment
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/appointments/APT001/tasks" `
    -Method Get

# Update Task
$updateTaskBody = @{
    status = "IN_PROGRESS"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/admin/tasks/TASK001" `
    -Method Put `
    -Body $updateTaskBody `
    -ContentType "application/json"

# Delete Task
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/tasks/TASK001" `
    -Method Delete
```

### Employee Operations
```powershell
# Create Employee
$employeeBody = @{
    name = "John Doe"
    email = "john.doe@example.com"
    phone = "+1234567890"
    specialization = "Engine Specialist"
    isAvailable = $true
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/admin/employees" `
    -Method Post `
    -Body $employeeBody `
    -ContentType "application/json"

# Update Employee
$updateEmployeeBody = @{
    name = "John Doe Updated"
    isAvailable = $false
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/admin/employees/EMP001" `
    -Method Put `
    -Body $updateEmployeeBody `
    -ContentType "application/json"

# Delete Employee
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/employees/EMP001" `
    -Method Delete
```

---

## HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 OK | Request successful |
| 201 Created | Resource created successfully |
| 204 No Content | Delete successful (no response body) |
| 400 Bad Request | Invalid data or business rule violation |
| 404 Not Found | Resource not found |
| 500 Internal Server Error | Server error |

---

## Complete Endpoint Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| **Appointments** | | |
| GET | `/api/admin/appointments/pending` | Get pending appointments |
| GET | `/api/admin/appointments` | Get all appointments |
| GET | `/api/admin/appointments/{id}` | Get appointment by ID |
| POST | `/api/admin/appointments` | Create appointment |
| PUT | `/api/admin/appointments/{id}` | Update appointment |
| DELETE | `/api/admin/appointments/{id}` | Delete appointment |
| PUT | `/api/admin/appointments/{id}/assign/{empId}` | Assign to employee |
| **Projects** | | |
| GET | `/api/admin/projects/pending` | Get pending projects |
| GET | `/api/admin/projects` | Get all projects |
| GET | `/api/admin/projects/{id}` | Get project by ID |
| POST | `/api/admin/projects` | Create project |
| PUT | `/api/admin/projects/{id}` | Update project |
| DELETE | `/api/admin/projects/{id}` | Delete project |
| PUT | `/api/admin/projects/{id}/assign/{empId}` | Assign to employee |
| **Tasks** | | |
| GET | `/api/admin/appointments/{id}/tasks` | Get tasks for appointment |
| GET | `/api/admin/tasks/{id}` | Get task by ID |
| POST | `/api/admin/tasks` | Create task |
| PUT | `/api/admin/tasks/{id}` | Update task |
| DELETE | `/api/admin/tasks/{id}` | Delete task |
| **Employees** | | |
| GET | `/api/admin/employees/available` | Get available employees |
| GET | `/api/admin/employees` | Get all employees |
| GET | `/api/admin/employees/{id}` | Get employee by ID |
| POST | `/api/admin/employees` | Create employee |
| PUT | `/api/admin/employees/{id}` | Update employee |
| DELETE | `/api/admin/employees/{id}` | Delete employee |

---

## Status Values Reference

### Appointment Status
- `PENDING` - Created, waiting for assignment
- `ASSIGNED` - Assigned to employee
- `CONFIRMED` - Employee confirmed
- `IN_PROGRESS` - Work in progress
- `COMPLETED` - Work completed
- `CANCELLED` - Cancelled
- `NO_SHOW` - Customer didn't show

### Project Status
- `PENDING` - Created, waiting for assignment
- `ASSIGNED` - Assigned to employee
- `IN_PROGRESS` - Work in progress
- `COMPLETED` - Work completed
- `CANCELLED` - Cancelled
- `ON_HOLD` - Temporarily paused

### Task Status
- `TODO` - Not started
- `IN_PROGRESS` - Being worked on
- `COMPLETED` - Finished
- `CANCELLED` - Cancelled

---

**Total Endpoints**: 28 REST API endpoints for complete admin CRUD operations!
