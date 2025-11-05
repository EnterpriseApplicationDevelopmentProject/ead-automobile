# Service Management API Documentation

This document describes the REST API endpoints for managing services in the EAD Automobile system.

## Overview

The Service Management feature allows administrators to create, update, view, and delete predefined services that customers can select when creating appointments. Each service includes:
- Name
- Description
- Price
- Estimated duration (in minutes)
- Image (uploaded to Cloudinary)
- Active status

## API Endpoints

### Admin Endpoints (Requires ADMIN role)

Base URL: `/api/admin/services`

#### 1. Create Service (without image)

**POST** `/api/admin/services`

Creates a new service without an image.

**Request Body:**
```json
{
  "name": "Oil Change",
  "description": "Complete oil change service including oil filter replacement",
  "price": 49.99,
  "estimatedDurationMinutes": 30,
  "isActive": true
}
```

**Response:** `201 Created`
```json
{
  "id": "uuid",
  "name": "Oil Change",
  "description": "Complete oil change service including oil filter replacement",
  "price": 49.99,
  "estimatedDurationMinutes": 30,
  "isActive": true,
  "imageUrl": null,
  "createdAt": "2025-11-05T10:30:00",
  "updatedAt": null
}
```

---

#### 2. Create Service (with image)

**POST** `/api/admin/services` (multipart/form-data)

Creates a new service with an image uploaded to Cloudinary.

**Request Parameters:**
- `service` (JSON string): Service data
- `image` (file): Image file (JPG, PNG, etc.)

**Example using cURL:**
```bash
curl -X POST "http://localhost:8080/api/admin/services" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "service={\"name\":\"Oil Change\",\"description\":\"Complete oil change service\",\"price\":49.99,\"estimatedDurationMinutes\":30,\"isActive\":true}" \
  -F "image=@oil-change.jpg"
```

**Example using Postman:**
1. Select POST method
2. Set URL: `http://localhost:8080/api/admin/services`
3. Select "Body" tab → "form-data"
4. Add key `service` (type: Text) with JSON value
5. Add key `image` (type: File) and select your image file

**Response:** `201 Created`
```json
{
  "id": "uuid",
  "name": "Oil Change",
  "description": "Complete oil change service including oil filter replacement",
  "price": 49.99,
  "estimatedDurationMinutes": 30,
  "isActive": true,
  "imageUrl": "https://res.cloudinary.com/.../services/uuid.jpg",
  "createdAt": "2025-11-05T10:30:00",
  "updatedAt": null
}
```

---

#### 3. Get Service by ID

**GET** `/api/admin/services/{id}`

Retrieves a single service by its ID.

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "name": "Oil Change",
  "description": "Complete oil change service including oil filter replacement",
  "price": 49.99,
  "estimatedDurationMinutes": 30,
  "isActive": true,
  "imageUrl": "https://res.cloudinary.com/.../services/uuid.jpg",
  "createdAt": "2025-11-05T10:30:00",
  "updatedAt": "2025-11-05T11:00:00"
}
```

---

#### 4. Get All Services (Admin View)

**GET** `/api/admin/services`

Retrieves all services including inactive ones.

**Response:** `200 OK`
```json
[
  {
    "id": "uuid1",
    "name": "Oil Change",
    "description": "Complete oil change service",
    "price": 49.99,
    "estimatedDurationMinutes": 30,
    "isActive": true,
    "imageUrl": "https://res.cloudinary.com/.../services/uuid1.jpg",
    "createdAt": "2025-11-05T10:30:00",
    "updatedAt": null
  },
  {
    "id": "uuid2",
    "name": "Tire Rotation",
    "description": "Professional tire rotation service",
    "price": 29.99,
    "estimatedDurationMinutes": 20,
    "isActive": false,
    "imageUrl": "https://res.cloudinary.com/.../services/uuid2.jpg",
    "createdAt": "2025-11-04T09:15:00",
    "updatedAt": "2025-11-05T08:45:00"
  }
]
```

---

#### 5. Get All Active Services

**GET** `/api/admin/services/active`

Retrieves only active services (available to all authenticated users).

**Response:** `200 OK`
```json
[
  {
    "id": "uuid1",
    "name": "Oil Change",
    "description": "Complete oil change service",
    "price": 49.99,
    "estimatedDurationMinutes": 30,
    "isActive": true,
    "imageUrl": "https://res.cloudinary.com/.../services/uuid1.jpg",
    "createdAt": "2025-11-05T10:30:00",
    "updatedAt": null
  }
]
```

---

#### 6. Update Service (without changing image)

**PUT** `/api/admin/services/{id}`

Updates a service without changing its image.

**Request Body:**
```json
{
  "name": "Premium Oil Change",
  "description": "Complete premium oil change service with synthetic oil",
  "price": 69.99,
  "estimatedDurationMinutes": 45,
  "isActive": true
}
```

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "name": "Premium Oil Change",
  "description": "Complete premium oil change service with synthetic oil",
  "price": 69.99,
  "estimatedDurationMinutes": 45,
  "isActive": true,
  "imageUrl": "https://res.cloudinary.com/.../services/uuid.jpg",
  "createdAt": "2025-11-05T10:30:00",
  "updatedAt": "2025-11-05T14:20:00"
}
```

---

#### 7. Update Service (with new image)

**PUT** `/api/admin/services/{id}/with-image` (multipart/form-data)

Updates a service and optionally replaces its image.

**Request Parameters:**
- `service` (JSON string): Updated service data
- `image` (file, optional): New image file

**Example using cURL:**
```bash
curl -X PUT "http://localhost:8080/api/admin/services/uuid/with-image" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "service={\"name\":\"Premium Oil Change\",\"description\":\"Updated description\",\"price\":69.99,\"estimatedDurationMinutes\":45,\"isActive\":true}" \
  -F "image=@new-oil-change.jpg"
```

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "name": "Premium Oil Change",
  "description": "Updated description",
  "price": 69.99,
  "estimatedDurationMinutes": 45,
  "isActive": true,
  "imageUrl": "https://res.cloudinary.com/.../services/new-uuid.jpg",
  "createdAt": "2025-11-05T10:30:00",
  "updatedAt": "2025-11-05T14:20:00"
}
```

---

#### 8. Soft Delete Service

**DELETE** `/api/admin/services/{id}`

Deactivates a service (sets `isActive` to false). The service remains in the database.

**Response:** `200 OK`
```json
{
  "message": "Service deactivated successfully"
}
```

---

#### 9. Permanently Delete Service

**DELETE** `/api/admin/services/{id}/permanent`

Permanently deletes a service from the database and removes its image from Cloudinary.

**Response:** `200 OK`
```json
{
  "message": "Service permanently deleted successfully"
}
```

---

#### 10. Toggle Service Status

**PATCH** `/api/admin/services/{id}/toggle-status`

Toggles the active status of a service (true ↔ false).

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "name": "Oil Change",
  "description": "Complete oil change service",
  "price": 49.99,
  "estimatedDurationMinutes": 30,
  "isActive": false,
  "imageUrl": "https://res.cloudinary.com/.../services/uuid.jpg",
  "createdAt": "2025-11-05T10:30:00",
  "updatedAt": "2025-11-05T15:10:00"
}
```

---

### Public Endpoints (No authentication required)

Base URL: `/api/customer/services`

#### 11. Get All Active Services (Customer)

**GET** `/api/customer/services`

Retrieves all active services for customers to view when creating appointments.

**Response:** `200 OK`
```json
[
  {
    "id": "uuid1",
    "name": "Oil Change",
    "description": "Complete oil change service",
    "price": 49.99,
    "estimatedDurationMinutes": 30,
    "isActive": true,
    "imageUrl": "https://res.cloudinary.com/.../services/uuid1.jpg",
    "createdAt": "2025-11-05T10:30:00",
    "updatedAt": null
  }
]
```

---

#### 12. Get Service by ID (Customer)

**GET** `/api/customer/services/{id}`

Retrieves a single active service by ID for customers.

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "name": "Oil Change",
  "description": "Complete oil change service",
  "price": 49.99,
  "estimatedDurationMinutes": 30,
  "isActive": true,
  "imageUrl": "https://res.cloudinary.com/.../services/uuid.jpg",
  "createdAt": "2025-11-05T10:30:00",
  "updatedAt": null
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Service with name 'Oil Change' already exists"
}
```

### 404 Not Found
```json
{
  "error": "Service not found with id: uuid"
}
```

### 500 Internal Server Error
```json
{
  "error": "Failed to create service: [error details]"
}
```

---

## Database Schema

**Table: `services`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | VARCHAR(36) | PRIMARY KEY, UUID |
| name | VARCHAR(255) | NOT NULL, UNIQUE |
| description | TEXT | |
| image_url | VARCHAR(500) | |
| image_public_id | VARCHAR(255) | |
| price | DECIMAL(10,2) | NOT NULL |
| estimated_duration_minutes | INT | NOT NULL |
| is_active | BOOLEAN | NOT NULL, DEFAULT true |
| created_at | TIMESTAMP | NOT NULL |
| updated_at | TIMESTAMP | |

---

## Integration with Appointment System

When customers create appointments, they can select from active services. The appointment system should:

1. Fetch active services using `/api/customer/services`
2. Display services with images, descriptions, and prices
3. Allow customers to select one or multiple services
4. Store the service IDs (or names) when creating the appointment
5. Calculate total estimated duration based on selected services

### Current Implementation Note

The existing `Appointment` entity stores services as a `List<String>` in the `tasks` field. You have two options for integration:

#### Option 1: Update Appointment to use Service IDs
Change the `tasks` field to store service IDs instead of service names:
```java
@ElementCollection
@CollectionTable(name = "appointment_services", joinColumns = @JoinColumn(name = "appointment_id"))
@Column(name = "service_id")
private List<String> serviceIds; // Store service UUIDs
```

#### Option 2: Keep using service names (simpler migration)
Continue using the current `tasks` field but populate it with service names from the Service entity:
```java
// In CreateAppointmentRequest
private List<String> tasks; // e.g., ["Oil Change", "Brake Inspection"]
```

### Recommended: Create Many-to-Many Relationship

For better data integrity and features, create a proper relationship:

```sql
CREATE TABLE appointment_services (
    appointment_id VARCHAR(36) NOT NULL,
    service_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (appointment_id, service_id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (service_id) REFERENCES services(id)
);
```

Then update the Appointment entity:
```java
@ManyToMany
@JoinTable(
    name = "appointment_services",
    joinColumns = @JoinColumn(name = "appointment_id"),
    inverseJoinColumns = @JoinColumn(name = "service_id")
)
private List<Service> services;
```

---

## Testing with Postman

### Collection Setup

1. Create a new collection: "Service Management API"
2. Add environment variables:
   - `baseUrl`: `http://localhost:8080`
   - `adminToken`: Your admin JWT token

### Example Requests

#### Create Service with Image
1. Method: POST
2. URL: `{{baseUrl}}/api/admin/services`
3. Headers:
   - `Authorization`: `Bearer {{adminToken}}`
4. Body (form-data):
   - `service`: 
   ```json
   {
     "name": "Brake Inspection",
     "description": "Comprehensive brake system inspection",
     "price": 39.99,
     "estimatedDurationMinutes": 25,
     "isActive": true
   }
   ```
   - `image`: (Select file)

---

## Security Configuration

Ensure your `SecurityConfig` allows:
- Admin endpoints: Require `ROLE_ADMIN`
- Customer endpoints: Allow authenticated customers (or public access)

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/customer/**").permitAll() // Or require authentication
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            // ... other configurations
        );
    return http.build();
}
```

---

## Cloudinary Configuration

Images are uploaded to: `ead-automobile/services/` folder
- Max width: 600px
- Max height: 400px
- Quality: auto
- Format: auto-optimized

---

## Best Practices

1. **Image Size**: Keep images under 5MB
2. **Image Format**: Use JPG or PNG
3. **Service Names**: Use unique, descriptive names
4. **Pricing**: Use decimal values with 2 decimal places
5. **Duration**: Estimate realistic time in minutes
6. **Soft Delete**: Use soft delete for historical data preservation
7. **Permanent Delete**: Only for test data or legal requirements

---

## Future Enhancements

1. Service categories/tags
2. Service availability scheduling
3. Service package bundles
4. Dynamic pricing based on vehicle type
5. Service reviews and ratings
6. Multi-language support for descriptions
