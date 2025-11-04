# System Architecture - Admin Module

## Overview Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                         FRONTEND (Your UI)                          │
│                                                                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐             │
│  │   View       │  │   View       │  │   Assign     │             │
│  │  Pending     │  │  Available   │  │  To Employee │             │
│  │ Appointments │  │  Employees   │  │              │             │
│  └──────────────┘  └──────────────┘  └──────────────┘             │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP REST API
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     CONTROLLER LAYER                                │
│                   (AdminController.java)                            │
│                                                                     │
│  ┌───────────────────────────────────────────────────────────┐    │
│  │  REST Endpoints:                                           │    │
│  │  • GET  /api/admin/appointments/pending                    │    │
│  │  • GET  /api/admin/projects/pending                        │    │
│  │  • GET  /api/admin/employees/available                     │    │
│  │  • PUT  /api/admin/appointments/{id}/assign/{empId}        │    │
│  │  • PUT  /api/admin/projects/{id}/assign/{empId}            │    │
│  └───────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              │ Calls Service Methods
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                                  │
│              (AdminService + AdminServiceImpl)                      │
│                                                                     │
│  ┌───────────────────────────────────────────────────────────┐    │
│  │  Business Logic:                                           │    │
│  │  • Fetch pending appointments/projects                     │    │
│  │  • Validate employee availability                          │    │
│  │  • Assign appointments/projects                            │    │
│  │  • Update status (PENDING → ASSIGNED)                      │    │
│  │  • Convert Entity ↔ DTO                                    │    │
│  └───────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              │ Uses Repositories
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    REPOSITORY LAYER                                 │
│        (AppointmentRepo, ProjectRepo, EmployeeRepo)                 │
│                                                                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌────────────────┐   │
│  │ Appointment      │  │ Project          │  │ Employee       │   │
│  │ Repository       │  │ Repository       │  │ Repository     │   │
│  │                  │  │                  │  │                │   │
│  │ • findPending    │  │ • findPending    │  │ • findAvail    │   │
│  │ • findByStatus   │  │ • findByStatus   │  │ • findBySpec   │   │
│  └──────────────────┘  └──────────────────┘  └────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              │ JPA/Hibernate
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         DATABASE LAYER                              │
│                      (PostgreSQL Database)                          │
│                                                                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌────────────────┐   │
│  │  Appointments    │  │    Projects      │  │   Employees    │   │
│  │  Table           │  │    Table         │  │   Table        │   │
│  │                  │  │                  │  │                │   │
│  │ • appointmentId  │  │ • projectId      │  │ • employeeId   │   │
│  │ • customerId     │  │ • customerId     │  │ • name         │   │
│  │ • status         │  │ • status         │  │ • isAvailable  │   │
│  │ • assignedEmpId  │  │ • assignedEmpId  │  │ • special...   │   │
│  └──────────────────┘  └──────────────────┘  └────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

## Data Flow Diagrams

### 1. View Pending Appointments Flow

```
Admin UI
   │
   │ GET /api/admin/appointments/pending
   │
   ▼
AdminController
   │
   │ getAllPendingAppointments()
   │
   ▼
AdminService
   │
   │ findAllPendingAppointments()
   │
   ▼
AppointmentRepository
   │
   │ Query: WHERE status = 'PENDING'
   │
   ▼
Database
   │
   │ Returns List<Appointment>
   │
   ▼
AdminService
   │
   │ Convert to List<AppointmentDTO>
   │
   ▼
AdminController
   │
   │ Return JSON Response
   │
   ▼
Admin UI (Displays pending appointments)
```

### 2. Assign Appointment Flow

```
Admin UI
   │
   │ PUT /api/admin/appointments/APT001/assign/EMP001
   │
   ▼
AdminController
   │
   │ assignAppointmentToEmployee(APT001, EMP001)
   │
   ▼
AdminService
   │
   ├─► appointmentRepository.findById(APT001)
   │   └─► Appointment found
   │
   ├─► employeeRepository.findById(EMP001)
   │   └─► Employee found
   │
   ├─► Validate: employee.isAvailable?
   │   └─► Yes, proceed
   │
   ├─► appointment.setAssignedEmployeeId(EMP001)
   ├─► appointment.setStatus(ASSIGNED)
   ├─► appointment.setUpdatedAt(now)
   │
   ├─► appointmentRepository.save(appointment)
   │   └─► Saved to Database
   │
   └─► Convert to AppointmentDTO
       │
       ▼
AdminController
   │
   │ Return JSON Response (Status: ASSIGNED)
   │
   ▼
Admin UI (Shows assignment success)
```

### 3. Integration Flow (Full System)

```
┌────────────────────────────────────────────────────────────┐
│                    CUSTOMER MODULE                         │
│              (Your Team Member's Part)                     │
│                                                            │
│  Customer creates Appointment/Project                     │
│     ├─► status = PENDING                                  │
│     ├─► assignedEmployeeId = NULL                         │
│     └─► Saved to Database                                 │
└────────────────────────────────────────────────────────────┘
                          │
                          │ Data exists in DB
                          ▼
┌────────────────────────────────────────────────────────────┐
│                     ADMIN MODULE                           │
│                   (Your Part - Now)                        │
│                                                            │
│  Admin Views Pending Items                                │
│     ├─► Fetches from Database                             │
│     └─► Displays in UI                                    │
│                                                            │
│  Admin Views Available Employees                          │
│     ├─► Fetches employees where isAvailable = true        │
│     └─► Displays in UI                                    │
│                                                            │
│  Admin Assigns to Employee                                │
│     ├─► Validates employee availability                   │
│     ├─► Updates assignedEmployeeId                        │
│     ├─► Changes status: PENDING → ASSIGNED                │
│     └─► Saves to Database                                 │
└────────────────────────────────────────────────────────────┘
                          │
                          │ Assignment complete
                          ▼
┌────────────────────────────────────────────────────────────┐
│                   EMPLOYEE MODULE                          │
│               (Future/Other Team Member)                   │
│                                                            │
│  Employee Views Assigned Items                            │
│     ├─► Query: WHERE assignedEmployeeId = {employeeId}    │
│     └─► Displays in UI                                    │
│                                                            │
│  Employee Updates Status                                  │
│     ├─► ASSIGNED → CONFIRMED                              │
│     ├─► CONFIRMED → IN_PROGRESS                           │
│     └─► IN_PROGRESS → COMPLETED                           │
└────────────────────────────────────────────────────────────┘
```

## Entity Relationships

```
┌─────────────────────┐
│     Customer        │
│─────────────────────│
│ customerId (PK)     │
│ name                │
│ email               │
│ ...                 │
└─────────────────────┘
         │ 1
         │
         │ creates
         │
         │ *
┌─────────────────────┐         ┌─────────────────────┐
│   Appointment       │    *    │     Employee        │
│─────────────────────│─────────│─────────────────────│
│ appointmentId (PK)  │  assigned│ employeeId (PK)     │
│ customerId (FK)     │    to   │ name                │
│ status              │─────────│ isAvailable         │
│ assignedEmployeeId  │    1    │ specialization      │
│ description         │         │ ...                 │
│ appointmentDateTime │         └─────────────────────┘
└─────────────────────┘                  │ 1
         │ 1                             │
         │                               │ handles
         │ for                           │
         │                               │ *
         │ *                    ┌─────────────────────┐
┌─────────────────────┐         │      Project        │
│      Vehicle        │         │─────────────────────│
│─────────────────────│    *    │ projectId (PK)      │
│ vehicleId (PK)      │─────────│ customerId (FK)     │
│ make                │ used in │ status              │
│ model               │─────────│ assignedEmployeeId  │
│ year                │    1    │ projectName         │
│ ...                 │         │ description         │
└─────────────────────┘         └─────────────────────┘
```

## Status Flow Diagrams

### Appointment Status Flow

```
┌─────────┐
│ PENDING │ ◄─── Customer creates appointment
└─────────┘
     │
     │ Admin assigns to employee
     ▼
┌─────────┐
│ASSIGNED │
└─────────┘
     │
     │ Employee confirms
     ▼
┌───────────┐
│ CONFIRMED │
└───────────┘
     │
     │ Work begins
     ▼
┌──────────────┐
│ IN_PROGRESS  │
└──────────────┘
     │
     │ Work finished
     ▼
┌───────────┐
│ COMPLETED │
└───────────┘

Alternative paths:
┌─────────┐
│CANCELLED│ ◄─── Can be cancelled anytime
└─────────┘

┌─────────┐
│ NO_SHOW │ ◄─── If customer doesn't show up
└─────────┘
```

### Project Status Flow

```
┌─────────┐
│ PENDING │ ◄─── Customer creates project
└─────────┘
     │
     │ Admin assigns to employee
     ▼
┌─────────┐
│ASSIGNED │
└─────────┘
     │
     │ Work begins
     ▼
┌──────────────┐
│ IN_PROGRESS  │◄──┐
└──────────────┘   │
     │             │ Resume
     │             │
     │ Pause       │
     ▼             │
┌─────────┐        │
│ ON_HOLD │────────┘
└─────────┘
     │
     │ Work finished
     ▼
┌───────────┐
│ COMPLETED │
└───────────┘

Alternative path:
┌─────────┐
│CANCELLED│ ◄─── Can be cancelled anytime
└─────────┘
```

## File Structure

```
ead-automobile/
│
├── src/main/java/com/example/ead_backend/
│   │
│   ├── controller/
│   │   └── AdminController.java          ✅ REST API endpoints
│   │
│   ├── service/
│   │   ├── AdminService.java             ✅ Service interface
│   │   └── impl/
│   │       └── AdminServiceImpl.java     ✅ Service implementation
│   │
│   ├── repository/
│   │   ├── AppointmentRepository.java    ✅ Appointment data access
│   │   ├── ProjectRepository.java        ✅ Project data access
│   │   └── EmployeeRepository.java       ✅ Employee data access
│   │
│   ├── model/
│   │   ├── entity/
│   │   │   ├── Appointment.java          ✅ Appointment entity
│   │   │   ├── Project.java              ✅ Project entity
│   │   │   └── Employee.java             ✅ Employee entity
│   │   │
│   │   └── enums/
│   │       ├── AppointmentStatus.java    ✅ Appointment statuses
│   │       └── ProjectStatus.java        ✅ Project statuses
│   │
│   └── dto/
│       ├── AppointmentDTO.java           ✅ Appointment DTO
│       ├── ProjectDTO.java               ✅ Project DTO
│       └── EmployeeDTO.java              ✅ Employee DTO
│
├── IMPLEMENTATION_SUMMARY.md             ✅ Implementation overview
├── ADMIN_IMPLEMENTATION_GUIDE.md         ✅ Detailed guide
├── API_TEST_EXAMPLES.md                  ✅ API testing guide
├── QUICK_START.md                        ✅ Quick setup guide
├── ARCHITECTURE.md                       ✅ This file
└── test-data.sql                         ✅ Sample test data
```

---

**Architecture Pattern**: Layered Architecture (MVC + Service Layer)
- **Controller Layer**: Handles HTTP requests/responses
- **Service Layer**: Business logic and validation
- **Repository Layer**: Data access
- **Entity Layer**: Database models
- **DTO Layer**: Data transfer objects

This architecture ensures:
- ✅ Separation of concerns
- ✅ Easy testing
- ✅ Maintainability
- ✅ Scalability
