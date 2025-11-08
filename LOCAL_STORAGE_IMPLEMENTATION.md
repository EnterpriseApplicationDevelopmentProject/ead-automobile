# Local File Storage Implementation

## Overview
This document describes the implementation of local file storage to replace Cloudinary for image uploads in the EAD Automobile application.

## Changes Made

### Backend (Java Spring Boot)

#### 1. New Service: LocalFileStorageService
**File**: `src/main/java/com/example/ead_backend/service/LocalFileStorageService.java`

- **Purpose**: Handles image upload, storage, retrieval, and deletion on local file system
- **Key Features**:
  - Uploads images to local directory with unique filenames (UUID-based)
  - Supports subdirectories (e.g., "customers", "vehicles")
  - Returns URLs in format: `http://localhost:8080/api/files/{subfolder}/{filename}`
  - Deletes old images when updating
  - Compatible with existing Cloudinary response format (returns `secure_url` and `public_id`)

#### 2. New Controller: FileController
**File**: `src/main/java/com/example/ead_backend/controller/FileController.java`

- **Purpose**: Serves uploaded images via HTTP
- **Endpoints**:
  - `GET /api/files/{filename}` - Serves files from root upload directory
  - `GET /api/files/{subfolder}/{filename}` - Serves files from subdirectories
- **Features**:
  - Automatic content-type detection
  - Returns 404 for missing files
  - CORS enabled for frontend access

#### 3. Updated Services

**CustomerProfileServiceImpl**:
- Replaced `CloudinaryService` with `LocalFileStorageService`
- Profile images stored in `uploads/customers/` directory
- Image URLs now point to local server: `http://localhost:8080/api/files/customers/{uuid}.jpg`

**VehicleServiceImpl**:
- Replaced `CloudinaryService` with `LocalFileStorageService`
- Vehicle images stored in `uploads/vehicles/` directory
- Image URLs now point to local server: `http://localhost:8080/api/files/vehicles/{uuid}.jpg`

#### 4. Configuration Changes

**application.properties**:
```properties
# Local File Storage Configuration
file.upload-dir=uploads
file.base-url=http://localhost:8080

# File Upload Settings
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

- Cloudinary configuration commented out (not removed, for reference)
- Upload directory set to `uploads/` (relative to project root)
- Base URL set to backend server URL
- Maximum file size: 10MB

#### 5. Directory Structure

Created upload directories:
```
ead-automobile/
  uploads/
    customers/     # Customer profile images
    vehicles/      # Vehicle images
```

### Database Schema
**No changes required** - The existing fields work perfectly:
- `profileImageUrl` / `imageUrl` - Now stores local URLs instead of Cloudinary URLs
- `profileImagePublicId` / `imagePublicId` - Now stores file paths (e.g., "customers/uuid.jpg")

### Frontend (Next.js)
**No changes required** - The frontend code remains unchanged because:
- API endpoints are the same
- Request/response formats are unchanged
- Image URLs are still absolute URLs (just pointing to local server instead of Cloudinary)

## How It Works

### Upload Flow
1. Frontend sends image file via multipart/form-data
2. Backend receives file in controller
3. `LocalFileStorageService.uploadImage()` is called:
   - Generates unique filename (UUID + extension)
   - Creates directory if doesn't exist
   - Saves file to disk
   - Returns URL and file path
4. URL and path saved to database
5. Response sent to frontend with image URL

### Retrieval Flow
1. Frontend requests image URL (e.g., `http://localhost:8080/api/files/customers/abc123.jpg`)
2. Request hits `FileController`
3. Controller:
   - Locates file on disk
   - Detects content type
   - Streams file to response
4. Browser displays image

### Update Flow
1. Frontend sends new image
2. Backend calls `updateImage()`:
   - Deletes old file from disk
   - Uploads new file
   - Returns new URL and path
3. Database updated with new values

### Delete Flow
1. Backend retrieves `imagePublicId` (file path)
2. Calls `deleteImage()` to remove file from disk
3. Database fields set to null

## Benefits

### ✅ Advantages
- **Simple**: No external dependencies or API keys
- **Fast**: Local file access is faster than cloud services
- **Free**: No cost for storage or bandwidth
- **Reliable**: No network issues or service outages
- **Private**: Images stored locally, not on third-party servers
- **Easy Debugging**: Can directly inspect uploaded files

### ⚠️ Considerations
- **Scalability**: Not suitable for distributed deployments (needs shared storage)
- **Backup**: Must backup uploads directory separately
- **Disk Space**: Monitor available disk space
- **Production**: For production, consider:
  - Network-attached storage (NAS)
  - Object storage (MinIO, AWS S3, etc.)
  - CDN for better performance

## Testing

### Test Customer Profile Image Upload
1. Login as customer
2. Navigate to Profile page
3. Click on profile photo to upload
4. Select an image file
5. Verify image uploads and displays correctly
6. Check `uploads/customers/` directory for saved file

### Test Vehicle Image Upload
1. Login as customer
2. Navigate to My Vehicles
3. Add or edit a vehicle
4. Upload vehicle image
5. Verify image uploads and displays correctly
6. Check `uploads/vehicles/` directory for saved file

### Verify Image URLs
- Profile image URL: `http://localhost:8080/api/files/customers/{uuid}.{ext}`
- Vehicle image URL: `http://localhost:8080/api/files/vehicles/{uuid}.{ext}`

## Troubleshooting

### Images Not Displaying
1. Check if backend server is running on port 8080
2. Verify file exists in `uploads/` directory
3. Check browser console for 404 errors
4. Verify CORS settings in `FileController`

### Upload Fails
1. Check file size (max 10MB)
2. Verify `uploads/` directory exists and is writable
3. Check backend logs for errors
4. Verify disk space available

### Old Cloudinary Images
- Existing database records with Cloudinary URLs will not display
- Option 1: Re-upload images using new system
- Option 2: Run a migration script to download and re-upload (if needed)

## File Locations

### Backend Files
- Service: `src/main/java/com/example/ead_backend/service/LocalFileStorageService.java`
- Controller: `src/main/java/com/example/ead_backend/controller/FileController.java`
- Updated Services: `src/main/java/com/example/ead_backend/service/impl/`
  - `CustomerProfileServiceImpl.java`
  - `VehicleServiceImpl.java`
- Config: `src/main/resources/application.properties`
- Upload Directory: `uploads/`

### Frontend Files
- **No changes made** - existing code works as-is
- Profile: `src/app/customer/profile/page.tsx`
- Vehicles: `src/app/customer/vehicles/page.tsx`
- Components: `src/components/profile/`, `src/components/vehicles/`

## Migration Notes

If you need to migrate from Cloudinary to local storage:

1. **Keep Cloudinary URLs**: Existing images will continue to work via Cloudinary URLs
2. **Gradual Migration**: New uploads use local storage, old images remain on Cloudinary
3. **Force Re-upload**: Ask users to re-upload images (simplest approach)
4. **Batch Download**: Create script to download all Cloudinary images to local storage (if needed)

## Production Deployment

For production environments, consider:

1. **External Storage**: Use MinIO, AWS S3, or Azure Blob Storage
2. **Shared Volume**: Mount shared network storage for multi-instance deployments
3. **CDN**: Add CDN in front of file server for better performance
4. **Backup Strategy**: Regular backups of uploads directory
5. **Monitoring**: Track disk usage and file count
6. **Security**: Add authentication/authorization for sensitive images

## Summary

The local file storage implementation successfully replaces Cloudinary with a simpler, more reliable solution suitable for development and small-scale deployments. The implementation is transparent to the frontend, requiring zero changes to existing code while providing faster, more reliable image uploads and retrieval.
