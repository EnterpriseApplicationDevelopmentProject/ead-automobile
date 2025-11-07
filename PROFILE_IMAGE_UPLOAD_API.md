# Customer Profile Image Upload API

## Overview
Customer profile image upload functionality using Cloudinary for cloud storage. Images are automatically optimized, stored securely, and can be updated or deleted.

## Features
✅ Upload profile images to Cloudinary  
✅ Automatic image optimization (800x600 max, auto quality)  
✅ Update profile image (old image automatically deleted)  
✅ Delete profile image  
✅ Secure HTTPS image URLs  
✅ Images organized in folder: `ead-automobile/customers/`  

---

## Backend Setup

### 1. Database Migration
Run the migration script to add image columns to the `customers` table:

```sql
ALTER TABLE customers 
ADD COLUMN profile_image_url VARCHAR(500) NULL,
ADD COLUMN profile_image_public_id VARCHAR(255) NULL;
```

### 2. Cloudinary Configuration
Ensure Cloudinary environment variables are set (see `CLOUDINARY_SETUP.md`):

```properties
cloudinary.cloud-name=your-cloud-name
cloudinary.api-key=your-api-key
cloudinary.api-secret=your-api-secret
```

---

## API Endpoints

### 1. Upload Profile Image
Upload or update the profile image for the authenticated customer.

**Endpoint:** `POST /api/customer/profile/me/image`

**Authentication:** Required (Customer role)

**Content-Type:** `multipart/form-data`

**Request Parameters:**
- `image` (File): Image file to upload

**Supported Formats:** JPEG, PNG, GIF, WebP

**Max File Size:** 10MB

**Response (200 OK):**
```json
{
  "id": 1,
  "userId": 5,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "1234567890",
  "profileImageUrl": "https://res.cloudinary.com/your-cloud/image/upload/v1234567890/ead-automobile/customers/abc-123.jpg",
  "profileImagePublicId": "ead-automobile/customers/abc-123"
}
```

**Error Responses:**

- **400 Bad Request** - Invalid file or file too large
```json
{
  "timestamp": "2025-11-07T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid image file"
}
```

- **401 Unauthorized** - Not authenticated
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required"
}
```

- **500 Internal Server Error** - Upload failed
```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Failed to upload image: Connection timeout"
}
```

---

### 2. Delete Profile Image
Delete the profile image for the authenticated customer.

**Endpoint:** `DELETE /api/customer/profile/me/image`

**Authentication:** Required (Customer role)

**Response (200 OK):**
```json
{
  "id": 1,
  "userId": 5,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "1234567890",
  "profileImageUrl": null,
  "profileImagePublicId": null
}
```

---

## Frontend Integration

### TypeScript Example

```typescript
import { uploadMyProfileImage, deleteMyProfileImage } from '@/lib/customerApi';

// Upload profile image
async function handleImageUpload(file: File) {
  try {
    const updatedProfile = await uploadMyProfileImage(file);
    console.log('Image uploaded:', updatedProfile.profileImageUrl);
  } catch (error) {
    console.error('Upload failed:', error.message);
  }
}

// Delete profile image
async function handleImageDelete() {
  try {
    const updatedProfile = await deleteMyProfileImage();
    console.log('Image deleted successfully');
  } catch (error) {
    console.error('Delete failed:', error.message);
  }
}

// Usage in component
<input 
  type="file" 
  accept="image/*" 
  onChange={(e) => {
    const file = e.target.files?.[0];
    if (file) handleImageUpload(file);
  }} 
/>
```

---

## Testing with Postman

### Upload Profile Image

1. **Method:** POST
2. **URL:** `http://localhost:8080/api/customer/profile/me/image`
3. **Headers:**
   - `Authorization: Bearer <your-jwt-token>`
4. **Body:** form-data
   - Key: `image` | Type: File | Value: Select image file

### Delete Profile Image

1. **Method:** DELETE
2. **URL:** `http://localhost:8080/api/customer/profile/me/image`
3. **Headers:**
   - `Authorization: Bearer <your-jwt-token>`

---

## Testing with cURL

### Upload Image
```bash
curl -X POST http://localhost:8080/api/customer/profile/me/image \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "image=@/path/to/profile-photo.jpg"
```

### Delete Image
```bash
curl -X DELETE http://localhost:8080/api/customer/profile/me/image \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Image Specifications

### Upload Process
1. User selects image file
2. Frontend converts to FormData
3. POST request to backend API
4. Backend uploads to Cloudinary
5. Cloudinary returns secure URL
6. Backend saves URL to database
7. Old image (if exists) is deleted from Cloudinary

### Optimization
- **Maximum dimensions:** 800x600 pixels
- **Crop mode:** Limit (maintains aspect ratio)
- **Quality:** Auto (Cloudinary optimizes)
- **Format:** Auto (Cloudinary chooses best format)

### Storage
- **Cloud provider:** Cloudinary
- **Folder structure:** `ead-automobile/customers/`
- **Naming:** UUID-based unique identifiers
- **Protocol:** HTTPS only

---

## Database Schema

### Customers Table
```sql
CREATE TABLE customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    phone_number VARCHAR(15) NOT NULL,
    profile_image_url VARCHAR(500) NULL,
    profile_image_public_id VARCHAR(255) NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Field Descriptions
- `profile_image_url`: Full HTTPS URL to Cloudinary image
- `profile_image_public_id`: Cloudinary public ID for deletion/updates

---

## Security Considerations

1. **Authentication Required:** All endpoints require valid JWT
2. **File Type Validation:** Only image files accepted
3. **File Size Limits:** Max 10MB per upload
4. **User Isolation:** Customers can only manage their own images
5. **Secure URLs:** All image URLs use HTTPS
6. **API Key Protection:** Cloudinary credentials in environment variables

---

## Error Handling

### Common Issues

**Issue:** "Failed to upload image"
- **Cause:** Cloudinary credentials incorrect or network issue
- **Solution:** Verify environment variables, check internet connection

**Issue:** "File too large"
- **Cause:** Image exceeds 10MB limit
- **Solution:** Compress image before upload

**Issue:** "Invalid file type"
- **Cause:** Unsupported file format
- **Solution:** Use JPEG, PNG, GIF, or WebP formats

**Issue:** "Unauthorized"
- **Cause:** Missing or invalid JWT token
- **Solution:** Log in again to refresh token

---

## Frontend Components

### ProfileCard Component
Displays profile photo with upload button:
- Shows current image or placeholder
- Upload progress indicator
- Error message display
- 3MB client-side validation

### ProfileClient Component
Manages profile data and image upload:
- Fetches current profile with image URL
- Handles image upload via API
- Updates local state after successful upload
- Error handling and retry logic

---

## Cloudinary Dashboard

### View Uploaded Images
1. Log in to [Cloudinary Console](https://cloudinary.com/console)
2. Go to **Media Library**
3. Navigate to folder: `ead-automobile/customers/`
4. View all uploaded profile images

### Monitor Usage
- **Storage:** Check used vs available storage
- **Bandwidth:** Monitor monthly transfer
- **Transformations:** Track image optimizations

---

## Future Enhancements

### Potential Improvements
1. **Image Cropping:** Add crop tool before upload
2. **Multiple Formats:** Support for different image sizes (thumbnail, full)
3. **Background Removal:** Automatic background removal option
4. **Face Detection:** Auto-center on detected faces
5. **Bulk Upload:** Admin feature to upload multiple images
6. **Image Filters:** Apply filters/effects to images
7. **Version History:** Keep previous profile images
8. **CDN Optimization:** Use Cloudinary's CDN for faster loading

---

## Related Documentation

- **Cloudinary Setup:** `CLOUDINARY_SETUP.md`
- **Customer Profile API:** `CUSTOMER_PROFILE_API.md`
- **Vehicle Image Upload:** `VEHICLE_IMAGE_API.md`
- **Database Migration:** `DATABASE_MIGRATION_PROFILE_IMAGE.sql`

---

## Changelog

**v1.0.0 - 2025-11-07**
- Initial release of profile image upload feature
- POST /api/customer/profile/me/image endpoint
- DELETE /api/customer/profile/me/image endpoint
- Frontend integration with ProfileCard component
- Database schema updates for image storage
- Cloudinary integration for cloud storage

---

## Support

For technical issues:
1. Check backend logs for error details
2. Verify Cloudinary credentials
3. Test with Postman to isolate frontend/backend issues
4. Check browser console for frontend errors
5. Review Cloudinary dashboard for upload status
