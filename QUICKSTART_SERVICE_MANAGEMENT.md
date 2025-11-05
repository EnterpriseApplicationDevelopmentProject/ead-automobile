# Quick Start Guide - Service Management Feature

## 🚀 Getting Started

This guide will help you quickly test and use the new service management feature.

## Prerequisites

- ✅ Java 17 or higher
- ✅ Maven
- ✅ PostgreSQL/MySQL database
- ✅ Cloudinary account (for image uploads)
- ✅ Admin user account

## Step 1: Configure Cloudinary

Add to your `application.properties`:

```properties
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

## Step 2: Run Database Migrations

The `Service` entity will be automatically created when you run the application. If using Flyway/Liquibase, create a migration:

```sql
CREATE TABLE services (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(500),
    image_public_id VARCHAR(255),
    price DECIMAL(10,2) NOT NULL,
    estimated_duration_minutes INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```

## Step 3: Start the Application

```bash
cd ead-automobile
mvn clean install
mvn spring-boot:run
```

## Step 4: Get Admin Token

1. Login as admin:
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "password": "your_password"
}
```

2. Copy the JWT token from the response

## Step 5: Create Your First Service

### Option A: Without Image

```bash
curl -X POST "http://localhost:8080/api/admin/services" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Oil Change",
    "description": "Complete oil change service",
    "price": 49.99,
    "estimatedDurationMinutes": 30,
    "isActive": true
  }'
```

### Option B: With Image

```bash
curl -X POST "http://localhost:8080/api/admin/services" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F 'service={"name":"Oil Change","description":"Complete oil change service","price":49.99,"estimatedDurationMinutes":30,"isActive":true}' \
  -F 'image=@/path/to/oil-change.jpg'
```

## Step 6: View Services

### As Admin (all services):
```bash
curl -X GET "http://localhost:8080/api/admin/services" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### As Customer (active services only):
```bash
curl -X GET "http://localhost:8080/api/customer/services"
```

## Step 7: Update a Service

```bash
curl -X PUT "http://localhost:8080/api/admin/services/{serviceId}" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Premium Oil Change",
    "description": "Premium synthetic oil change",
    "price": 69.99,
    "estimatedDurationMinutes": 45,
    "isActive": true
  }'
```

## 📱 Using Postman

1. Import the collection: `postman-collection-service-management.json`
2. Set environment variables:
   - `baseUrl`: `http://localhost:8080`
   - `adminToken`: Your JWT token
   - `serviceId`: UUID of a service
3. Start testing endpoints!

## 🎯 Common Use Cases

### Create Multiple Services

Create a variety of services for your automobile business:

```json
// Oil Change
{
  "name": "Oil Change",
  "description": "Complete oil change with premium synthetic oil",
  "price": 49.99,
  "estimatedDurationMinutes": 30,
  "isActive": true
}

// Brake Inspection
{
  "name": "Brake Inspection",
  "description": "Comprehensive brake system inspection and service",
  "price": 79.99,
  "estimatedDurationMinutes": 45,
  "isActive": true
}

// Tire Rotation
{
  "name": "Tire Rotation",
  "description": "Professional tire rotation and balancing",
  "price": 29.99,
  "estimatedDurationMinutes": 20,
  "isActive": true
}

// Engine Diagnostic
{
  "name": "Engine Diagnostic",
  "description": "Complete engine diagnostic scan and analysis",
  "price": 99.99,
  "estimatedDurationMinutes": 60,
  "isActive": true
}

// Air Conditioning Service
{
  "name": "AC Service",
  "description": "Air conditioning system check and recharge",
  "price": 89.99,
  "estimatedDurationMinutes": 50,
  "isActive": true
}
```

### Deactivate Seasonal Services

For services that are seasonal:

```bash
# Deactivate winter services in summer
curl -X DELETE "http://localhost:8080/api/admin/services/{winterServiceId}" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Reactivate when needed
curl -X PATCH "http://localhost:8080/api/admin/services/{winterServiceId}/toggle-status" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Update Service Pricing

```bash
curl -X PUT "http://localhost:8080/api/admin/services/{serviceId}" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Oil Change",
    "description": "Complete oil change service",
    "price": 59.99,
    "estimatedDurationMinutes": 30,
    "isActive": true
  }'
```

## 🔍 Testing Endpoints

### Test 1: Create Service
```bash
POST /api/admin/services
Expected: 201 Created with service data
```

### Test 2: Get All Services
```bash
GET /api/admin/services
Expected: 200 OK with array of services
```

### Test 3: Get Active Services (Customer)
```bash
GET /api/customer/services
Expected: 200 OK with array of active services
```

### Test 4: Toggle Status
```bash
PATCH /api/admin/services/{id}/toggle-status
Expected: 200 OK with updated service
```

### Test 5: Delete Service
```bash
DELETE /api/admin/services/{id}
Expected: 200 OK with success message
```

## 🐛 Troubleshooting

### Issue: "Service with name already exists"
**Solution**: Use a unique service name or update the existing service instead.

### Issue: "Failed to upload image"
**Solution**: 
- Check Cloudinary credentials in `application.properties`
- Ensure image file is less than 10MB
- Verify image format (JPG, PNG, etc.)

### Issue: "403 Forbidden"
**Solution**: 
- Ensure you're using a valid admin token
- Check that the user has ROLE_ADMIN
- Verify SecurityConfig allows admin endpoints

### Issue: "Service not found"
**Solution**: 
- Verify the service ID is correct
- Check if service was permanently deleted
- Use GET /api/admin/services to list all services

## 📊 Database Queries

### View all services:
```sql
SELECT * FROM services;
```

### View only active services:
```sql
SELECT * FROM services WHERE is_active = true;
```

### Find expensive services:
```sql
SELECT * FROM services WHERE price > 50.00 ORDER BY price DESC;
```

### Services by duration:
```sql
SELECT name, estimated_duration_minutes FROM services 
ORDER BY estimated_duration_minutes DESC;
```

## 🎨 Image Best Practices

1. **Size**: Keep images under 5MB
2. **Dimensions**: Recommended 600x400 or similar ratio
3. **Format**: JPG or PNG
4. **Content**: Clear, professional service images
5. **Naming**: Descriptive filenames (e.g., oil-change-service.jpg)

## 🔐 Security Notes

1. **Admin Endpoints**: Only accessible with ROLE_ADMIN
2. **Public Endpoints**: Accessible without authentication
3. **Token**: Include `Authorization: Bearer {token}` header for admin endpoints
4. **CORS**: Configure CORS for frontend integration

## 📈 Next Steps

1. ✅ Create 5-10 common services
2. ✅ Upload images for each service
3. ✅ Test all CRUD operations
4. ✅ Integrate with appointment system
5. ✅ Create frontend UI for service selection
6. ✅ Monitor Cloudinary usage
7. ✅ Set up automated backups

## 🔗 Related Documentation

- Full API Documentation: `SERVICE_MANAGEMENT_API.md`
- Implementation Details: `SERVICE_MANAGEMENT_IMPLEMENTATION.md`
- Cloudinary Setup: `CLOUDINARY_SETUP.md`

## 💡 Tips

- Use descriptive service names
- Keep descriptions concise but informative
- Price should include 2 decimal places
- Estimate duration realistically
- Use soft delete to preserve history
- Upload high-quality service images
- Test with public endpoints before going live

## 🎉 You're Ready!

You now have a fully functional service management system. Start creating services and integrate them with your appointment booking system!

For questions or issues, refer to the detailed API documentation or check the error logs.

Happy coding! 🚀
