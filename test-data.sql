-- Sample Test Data for Admin Module Testing
-- This script provides initial test data for the EAD Automobile System

-- =====================================================
-- 0. CLEAN UP EXISTING DATA (Optional - comment out if not needed)
-- =====================================================
-- DELETE FROM tasks;
-- DELETE FROM appointments;
-- DELETE FROM projects;
-- DELETE FROM employees;
-- DELETE FROM vehicles;
-- DELETE FROM customers;

-- =====================================================
-- 1. INSERT SAMPLE CUSTOMERS
-- =====================================================
INSERT INTO customers (customer_id)
VALUES 
    ('CUST001'),
    ('CUST002'),
    ('CUST003'),
    ('CUST004'),
    ('CUST005'),
    ('CUST006'),
    ('CUST007'),
    ('CUST008')
ON CONFLICT (customer_id) DO NOTHING;

-- =====================================================
-- 2. INSERT SAMPLE VEHICLES
-- =====================================================
INSERT INTO vehicles (vehicle_id)
VALUES 
    ('VEH001'),
    ('VEH002'),
    ('VEH003'),
    ('VEH004'),
    ('VEH005'),
    ('VEH006'),
    ('VEH007'),
    ('VEH008')
ON CONFLICT (vehicle_id) DO NOTHING;

-- =====================================================
-- 3. INSERT SAMPLE EMPLOYEES
-- =====================================================
INSERT INTO employees (employee_id, name, email, phone, specialization, is_available, created_at, updated_at)
VALUES 
    ('EMP001', 'John Doe', 'john.doe@automobile.com', '+1-555-0101', 'Engine Specialist', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('EMP002', 'Jane Smith', 'jane.smith@automobile.com', '+1-555-0102', 'Electrical Systems', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('EMP003', 'Mike Johnson', 'mike.johnson@automobile.com', '+1-555-0103', 'Body Work & Paint', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('EMP004', 'Sarah Williams', 'sarah.williams@automobile.com', '+1-555-0104', 'Transmission Expert', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('EMP005', 'David Brown', 'david.brown@automobile.com', '+1-555-0105', 'Brake Systems', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (employee_id) DO NOTHING;

-- =====================================================
-- 4. INSERT SAMPLE APPOINTMENTS (PENDING - Created by Customers)
-- =====================================================
-- These appointments are waiting for admin to assign to employees

INSERT INTO appointments (appointment_id, customer_id, vehicle_id, description, appointment_date_time, status, assigned_employee_id, created_at, updated_at)
VALUES 
    ('APT001', 'CUST001', 'VEH001', 'Regular oil change and filter replacement', '2025-11-10 10:00:00', 'PENDING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('APT002', 'CUST002', 'VEH002', 'Brake inspection and service', '2025-11-11 14:00:00', 'PENDING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('APT003', 'CUST003', 'VEH003', 'Engine diagnostic check - strange noise', '2025-11-12 09:00:00', 'PENDING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('APT004', 'CUST001', 'VEH001', 'Tire rotation and alignment', '2025-11-13 11:00:00', 'PENDING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('APT005', 'CUST004', 'VEH004', 'Air conditioning not working', '2025-11-15 15:00:00', 'PENDING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (appointment_id) DO NOTHING;

-- =====================================================
-- 5. INSERT SAMPLE PROJECTS (PENDING - Created by Customers)
-- =====================================================
-- These projects are waiting for admin to assign to employees

INSERT INTO projects (project_id, customer_id, vehicle_id, project_name, description, start_date, expected_end_date, status, assigned_employee_id, created_at, updated_at)
VALUES 
    ('PRJ001', 'CUST001', 'VEH001', 'Complete Engine Overhaul', 'Full engine teardown, inspection, and rebuild with new gaskets and seals', '2025-11-15 08:00:00', '2025-11-22 17:00:00', 'PENDING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('PRJ002', 'CUST003', 'VEH003', 'Full Body Paint & Restoration', 'Complete body paint job with rust removal and minor dent repair', '2025-11-18 09:00:00', '2025-11-25 16:00:00', 'PENDING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('PRJ003', 'CUST005', 'VEH005', 'Transmission Rebuild', 'Complete transmission overhaul with torque converter replacement', '2025-11-20 08:00:00', '2025-11-27 17:00:00', 'PENDING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('PRJ004', 'CUST002', 'VEH002', 'Suspension Upgrade', 'Install performance suspension kit with new shocks and springs', '2025-11-16 10:00:00', '2025-11-18 15:00:00', 'PENDING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (project_id) DO NOTHING;

-- =====================================================
-- 6. INSERT SOME ASSIGNED APPOINTMENTS (for testing view all)
-- =====================================================

INSERT INTO appointments (appointment_id, customer_id, vehicle_id, description, appointment_date_time, status, assigned_employee_id, created_at, updated_at)
VALUES 
    ('APT006', 'CUST006', 'VEH006', 'Battery replacement', '2025-11-08 10:00:00', 'ASSIGNED', 'EMP002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('APT007', 'CUST007', 'VEH007', 'Windshield wiper replacement', '2025-11-09 13:00:00', 'COMPLETED', 'EMP003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (appointment_id) DO NOTHING;

-- =====================================================
-- 7. INSERT SOME ASSIGNED PROJECTS (for testing view all)
-- =====================================================

INSERT INTO projects (project_id, customer_id, vehicle_id, project_name, description, start_date, expected_end_date, status, assigned_employee_id, created_at, updated_at)
VALUES 
    ('PRJ005', 'CUST006', 'VEH006', 'Custom Exhaust System', 'Install custom performance exhaust system', '2025-11-10 08:00:00', '2025-11-12 17:00:00', 'ASSIGNED', 'EMP001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('PRJ006', 'CUST008', 'VEH008', 'Interior Detailing', 'Complete interior deep clean and detailing', '2025-11-05 09:00:00', '2025-11-06 15:00:00', 'COMPLETED', 'EMP003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (project_id) DO NOTHING;

-- =====================================================
-- 8. INSERT SAMPLE TASKS FOR APPOINTMENTS
-- =====================================================

INSERT INTO tasks (task_id, appointment_id, task_name, description, assigned_employee_id, status, due_date, created_at, updated_at)
VALUES 
    ('TASK001', 'APT001', 'Drain old oil', 'Remove oil drain plug and drain old oil', 'EMP001', 'TODO', '2025-11-10 10:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TASK002', 'APT001', 'Replace oil filter', 'Remove old filter and install new one', 'EMP001', 'TODO', '2025-11-10 10:45:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TASK003', 'APT001', 'Add new oil', 'Fill with specified amount of new oil', 'EMP001', 'TODO', '2025-11-10 11:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TASK004', 'APT002', 'Inspect brake pads', 'Check front and rear brake pad thickness', NULL, 'TODO', '2025-11-11 14:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TASK005', 'APT002', 'Check brake fluid', 'Inspect brake fluid level and condition', NULL, 'TODO', '2025-11-11 15:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (task_id) DO NOTHING;

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Check employees
SELECT * FROM employees ORDER BY employee_id;

-- Check pending appointments (should be visible to admin)
SELECT * FROM appointments WHERE status = 'PENDING' ORDER BY appointment_date_time;

-- Check pending projects (should be visible to admin)
SELECT * FROM projects WHERE status = 'PENDING' ORDER BY start_date;

-- Check all appointments
SELECT * FROM appointments ORDER BY appointment_id;

-- Check all projects
SELECT * FROM projects ORDER BY project_id;

-- Check available employees
SELECT * FROM employees WHERE is_available = true;

-- =====================================================
-- CLEANUP SCRIPT (Use if you need to reset data)
-- =====================================================

-- Uncomment below to delete all test data
/*
DELETE FROM appointments WHERE appointment_id LIKE 'APT%';
DELETE FROM projects WHERE project_id LIKE 'PRJ%';
DELETE FROM employees WHERE employee_id LIKE 'EMP%';
*/

-- =====================================================
-- NOTES
-- =====================================================

-- 1. Make sure customers and vehicles exist before inserting appointments/projects
--    or adjust the CUSTOMER_ID and VEHICLE_ID fields to match your data

-- 2. The appointment_date_time and start_date/expected_end_date are set to future dates
--    Adjust as needed for your testing

-- 3. Employee specializations:
--    - Engine Specialist: For engine-related work
--    - Electrical Systems: For electrical issues
--    - Body Work & Paint: For body and paint jobs
--    - Transmission Expert: For transmission work
--    - Brake Systems: For brake-related work

-- 4. Status values:
--    Appointments: PENDING, ASSIGNED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
--    Projects: PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED, ON_HOLD

-- 5. To test the admin functionality:
--    a. Use GET endpoints to view pending appointments/projects
--    b. Use GET endpoints to view available employees
--    c. Use PUT endpoints to assign appointments/projects to employees
--    d. Verify status changes from PENDING to ASSIGNED
