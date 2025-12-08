# Property Management System

A comprehensive RESTful API for managing properties with JWT-based authentication and role-based access control (RBAC). Built with Spring Boot 3.5.7 and Java 17.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Authentication & Authorization](#authentication--authorization)
- [Role-Based Access Control](#role-based-access-control)
- [Project Structure](#project-structure)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Security Considerations](#security-considerations)
- [Future Enhancements](#future-enhancements)

## ✨ Features

- **User Management**
  - User registration with address information (stored in separate AddressEntity with OneToOne relationship)
  - Secure login with JWT token generation
  - Password encryption using BCrypt
  - Support for multiple roles per user

- **Property Management**
  - Create, read, update, and delete properties
  - Search properties by title
  - Search properties by price range
  - View all properties or filter by user

- **Security**
  - JWT-based stateless authentication
  - Role-based access control (RBAC)
  - Password hashing with BCrypt
  - Secure API endpoints with ownership validation

- **API Documentation**
  - Swagger/OpenAPI integration (SpringDoc OpenAPI 2.8.13)
  - Interactive API documentation
  - OpenAPI JSON specification

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Security**: Spring Security with JWT
- **Database**: 
  - H2 (file-based, for development)
  - MySQL 8.0+ (for production)
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: SpringDoc OpenAPI 2.8.13
- **MySQL Connector**: 8.0.33
- **Build Tool**: Maven
- **Other Libraries**:
  - JWT (JSON Web Token) - jjwt 0.11.5
  - Lombok
  - Spring Boot Actuator
  - Apache Commons Lang3 3.18.0

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+** (for production)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code)

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd property-management
```

### 2. Configure Database

#### For Development - Default (H2 - File-Based)
The application uses H2 database by default in the `local` profile (which is the default active profile). The database file is stored at `~/h2/propertydb`. **No additional configuration needed** - this is the quickest way to get started.

#### For Development - Alternative (MySQL)
If you prefer to use MySQL for development, switch to the `dev` profile:

1. Create a MySQL database:
```sql
CREATE DATABASE property-dev;
```

2. Update `application-dev.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/property-dev
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Set the active profile to `dev` in `application.properties`:
```properties
spring.profiles.active=dev
```

#### For Production (MySQL)
1. Create a MySQL database:
```sql
CREATE DATABASE property_management;
```

2. Update `application-prod.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/property_management
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Configure JWT Secret Key

**Important**: Generate a secure secret key for production!

1. Generate a Base64-encoded secret key (at least 256 bits):
```bash
# Using OpenSSL
openssl rand -base64 64
```

2. Update `application.properties`:
```properties
jwt.secret=your_generated_secret_key_here
jwt.validity=86400000  # 24 hours in milliseconds
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/property-management-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8089**

## ⚙️ Configuration

### Application Properties

The project uses Spring profiles for different environments:

- **local** - **Default development profile** with H2 file-based database (`~/h2/propertydb`), H2 console enabled. No database setup required - perfect for quick start.
- **dev** - Alternative development profile with MySQL database (`property-dev`), H2 console disabled. Requires MySQL setup.
- **test** - Testing environment with MySQL for automated tests
- **acc** - Acceptance testing environment (UAT) with MySQL, pre-production validation environment
- **prod** - Production environment (secure configuration)

Set the active profile in `application.properties`:
```properties
spring.profiles.active=local
```

### Environment-Specific Details

#### Local Profile (Default Development)
- **Default active profile** - no configuration needed
- Database: H2 file-based (`jdbc:h2:file:~/h2/propertydb`)
- Console: Enabled at `/h2-console`
- SQL logging: Enabled
- **Best for**: Quick start, local development, no database setup required

#### Dev Profile (Alternative Development)
- Requires MySQL database setup
- Database: MySQL (`property-dev`)
- Console: Disabled (uses MySQL, not H2)
- SQL logging: Enabled
- **Best for**: Development with MySQL, testing MySQL-specific features

#### Test Profile
- Database: MySQL (test database)
- Console: Disabled for automated testing
- SQL logging: Disabled (can be enabled for debugging if needed)
- **Best for**: Running unit and integration tests

#### Acc Profile (Acceptance Testing)
- Database: MySQL (`property-dev` or dedicated acceptance database)
- Console: Disabled (production-like security)
- SQL logging: Disabled (production-like configuration)
- **Best for**: User acceptance testing (UAT), pre-production validation, stakeholder testing
- **Note**: This environment mimics production but allows for testing and validation before going live

#### Production Profile
- Database: MySQL (configure your credentials)
- Console: Disabled for security
- SQL logging: Disabled (recommended)
- **Best for**: Live production environment

### JWT Configuration

```properties
# JWT Secret Key (Base64 encoded, at least 256 bits)
jwt.secret=your_secret_key_here

# Token validity in milliseconds (default: 60000 = 1 minute)
# Recommended for production: 86400000 (24 hours)
jwt.validity=60000
```

### Server Configuration

```properties
server.port=8089
spring.application.name=property-management
```

### Input Validation

The following validations are enforced at both DTO and Entity levels:

- **Email**: Required, 1-50 characters, must be unique
- **Password**: Required, cannot be empty
- **Property Title**: Required, max 20 characters (validated at DTO level with @NotNull, @NotEmpty, @Size)
- **Property Description**: Optional, max 40 characters (validated at DTO level with @Size)
- **Price**: Required, numeric value (validated at DTO level with @NotNull)
- **Address**: Required for properties (validated at DTO level with @NotNull, @NotEmpty)

**Note**: Validation occurs at the DTO level (using Jakarta Bean Validation annotations) and is also enforced at the Entity level through JPA column constraints. Invalid requests return 400 Bad Request with detailed error messages.

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

### Swagger UI
**URL**: **http://localhost:8089/swagger-ui/index.html**

The Swagger UI provides:
- Interactive API testing
- Request/response schemas
- Authentication requirements
- Example requests
- Try-it-out functionality

**Note**: Swagger UI is publicly accessible (no authentication required). This is intentional for development. Consider securing it in production environments.

### OpenAPI JSON Specification

The OpenAPI 3.0 specification in JSON format is available at:
**http://localhost:8089/v3/api-docs**

This can be used to:
- Generate client SDKs
- Import into API testing tools (Postman, Insomnia)
- Generate documentation
- Integrate with API gateway services

## 🔐 Authentication & Authorization

### Registration

**Endpoint**: `POST /api/v1/users/registerUser`

**Request Body**:
```json
{
  "ownerName": "John Doe",
  "ownerEmail": "john@example.com",
  "password": "password123",
  "phone": "1234567890",
  "houseNo": "123",
  "street": "Main Street",
  "postalCode": "12345",
  "country": "USA",
  "roles": ["USER", "PROPERTY_OWNER"]
}
```

**Response** (201 Created):
```json
{
  "id": "abc-123-uuid",
  "ownerName": "John Doe",
  "ownerEmail": "john@example.com",
  "phone": "1234567890",
  "roles": ["USER", "PROPERTY_OWNER"]
}
```

**Note**: 
- If `roles` is not provided, the user will be assigned the default `USER` role
- Address information is stored in a separate `AddressEntity` with a OneToOne relationship
- User IDs are generated as UUIDs (String type)

**Error Response** (400 Bad Request):
```json
{
  "errors": [
    {
      "code": "EXISTING_USER",
      "message": "User already exists."
    }
  ]
}
```

### Login

**Endpoint**: `POST /api/v1/users/loginUser`

**Request Body**:
```json
{
  "ownerEmail": "john@example.com",
  "password": "password123"
}
```

**Response** (200 OK): JWT Token (plain text string)
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNj...
```

**Error Response** (400 Bad Request):
```json
{
  "errors": [
    {
      "code": "INVALID_LOGIN",
      "message": "Incorrect username or password"
    }
  ]
}
```

### Using JWT Token

Include the token in the `Authorization` header for protected endpoints:

```
Authorization: Bearer <your_jwt_token>
```

**Important**: 
- Token expires after the configured validity period (default: 60 seconds)
- Include the word "Bearer" followed by a space before the token
- Store tokens securely on the client side (httpOnly cookies recommended)

### Error Response Format

All error responses follow this structure:

```json
{
  "errors": [
    {
      "code": "ERROR_CODE",
      "message": "Human-readable error message"
    }
  ]
}
```

**Common Error Codes**:
- `EXISTING_USER` - User already exists (registration)
- `INVALID_LOGIN` - Incorrect username or password
- `UNAUTHORIZED` - Insufficient permissions
- `PROPERTY_NOT_FOUND` - Property doesn't exist
- `NON_EXISTING_USER_ID` - User ID not found
- `INTERNAL_ERROR` - Server error

## 👥 Role-Based Access Control

The system supports three roles with different access levels:

### 1. **ADMIN**
- ✅ Full access to all operations
- ✅ Can create, update, and delete any property
- ✅ Can view all properties
- ✅ Can manage all users
- ✅ Bypasses ownership checks

### 2. **PROPERTY_OWNER**
- ✅ Can add new properties (must use their own user ID)
- ✅ Can view only their own properties
- ✅ Can delete only their own properties (ownership verified by email)
- ❌ Cannot update properties (admin only)
- ❌ Cannot view other users' properties
- ❌ Cannot delete other users' properties (returns 401 Unauthorized)

### 3. **USER**
- ✅ Can view all properties
- ✅ Can search properties by title
- ✅ Can search properties by price range
- ❌ Cannot add properties
- ❌ Cannot delete properties
- ❌ Cannot update properties

### Property Ownership Validation

The system validates property ownership by:
- Matching the authenticated user's email with the property's owner email
- PROPERTY_OWNER role users can only manage properties where they are the owner
- ADMIN role users can manage any property regardless of ownership
- Ownership checks are enforced at the service layer for additional security

### Multiple Roles

Users can have multiple roles simultaneously. For example:
- A user with `["USER", "PROPERTY_OWNER"]` can:
  - View all properties (USER role)
  - Add their own properties (PROPERTY_OWNER role)
  - Delete their own properties (PROPERTY_OWNER role)

## 📁 Project Structure

```
property-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mycompany/property_management/
│   │   │       ├── config/              # Security & JWT configuration
│   │   │       │   ├── CustomUserDetailsService.java
│   │   │       │   ├── JwtRequestFilter.java
│   │   │       │   ├── JwtTokenUtil.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controller/          # REST controllers
│   │   │       │   ├── PropertyController.java
│   │   │       │   └── UserController.java
│   │   │       ├── converter/          # Entity-DTO converters
│   │   │       │   ├── PropertyConverter.java
│   │   │       │   └── UserConverter.java
│   │   │       ├── entity/              # JPA entities
│   │   │       │   ├── AddressEntity.java
│   │   │       │   ├── PropertyEntity.java
│   │   │       │   ├── Role.java (enum)
│   │   │       │   └── UserEntity.java
│   │   │       ├── exception/           # Exception handling
│   │   │       │   ├── BusinessException.java
│   │   │       │   ├── CustomExceptionHandler.java
│   │   │       │   └── ErrorModel.java
│   │   │       ├── model/               # DTOs
│   │   │       │   ├── PropertiesDto.java
│   │   │       │   ├── PropertyDto.java
│   │   │       │   └── UserDto.java
│   │   │       ├── repository/          # Data access layer
│   │   │       │   ├── AddressRepository.java
│   │   │       │   ├── PropertyRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       ├── service/             # Business logic
│   │   │       │   ├── PropertyService.java
│   │   │       │   └── UserService.java
│   │   │       └── PropertyManagementApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-*.properties  # Environment-specific configs
│   │       └── logback-spring.xml
│   └── test/                             # Test files
└── pom.xml
```

### Database Schema

The application creates the following tables:

- **User_Table** - User information
  - `id` (String/UUID) - Primary key
  - `Owner_Name` (VARCHAR 20)
  - `Owner_Email` (VARCHAR 50, unique)
  - `Phone_Number` (VARCHAR 10)
  - `Owner_Password` (encrypted)

- **Property_Table** - Property listings
  - `id` (Long) - Primary key (auto-increment)
  - `Title` (VARCHAR 20)
  - `Description` (VARCHAR 40, nullable)
  - `Price` (Double)
  - `Address` (VARCHAR 20)
  - `USER_ID` (Foreign key to User_Table)

- **Address_Table** - User addresses (OneToOne with User)
  - `id` (Long) - Primary key
  - `House_Number`
  - `Street`
  - `Postal_Code`
  - `Country`
  - `USER_ID` (Foreign key to User_Table)

- **user_roles** - User role mappings (collection table)
  - `user_id` (Foreign key)
  - `role` (VARCHAR - enum value)

**Note**: Property IDs are of type `Long` (auto-increment), while User IDs are of type `String` (UUID).

## 🏃 Running the Application

### Development Mode

```bash
# Using Maven
mvn spring-boot:run

# Using Java
java -jar target/property-management-0.0.1-SNAPSHOT.jar
```

### Production Mode

```bash
# Set production profile
java -jar -Dspring.profiles.active=prod property-management-0.0.1-SNAPSHOT.jar
```

### H2 Database Console (Local Profile - Default Development)

When using the `local` profile (default development profile), you can access the H2 database console:

- **URL**: http://localhost:8089/h2-console
- **JDBC URL**: `jdbc:h2:file:~/h2/propertydb`
- **Username**: `sa`
- **Password**: (leave empty)

**Note**: H2 console is available only in the `local` profile (default development profile). The `dev` profile uses MySQL and has H2 console disabled. H2 console should be disabled in production for security reasons.

### Health Check

Once running, check the application health:

- **Health**: http://localhost:8089/actuator/health
- **Metrics**: http://localhost:8089/actuator/metrics
- **Info**: http://localhost:8089/actuator/info

## 🧪 Testing

### Using Swagger UI

1. Start the application
2. Navigate to http://localhost:8089/swagger-ui/index.html
3. Click "Authorize" button and enter your JWT token (format: `Bearer <token>`)
4. Test endpoints directly from the browser

### Using cURL

#### Register a User

```bash
curl -X POST http://localhost:8089/api/v1/users/registerUser \
  -H "Content-Type: application/json" \
  -d '{
    "ownerName": "John Doe",
    "ownerEmail": "john@example.com",
    "password": "password123",
    "phone": "1234567890",
    "houseNo": "123",
    "street": "Main St",
    "postalCode": "12345",
    "country": "USA"
  }'
```

**Response**:
```json
{
  "id": "abc-123-uuid",
  "ownerName": "John Doe",
  "ownerEmail": "john@example.com",
  "phone": "1234567890",
  "roles": ["USER"]
}
```

#### Login

```bash
curl -X POST http://localhost:8089/api/v1/users/loginUser \
  -H "Content-Type: application/json" \
  -d '{
    "ownerEmail": "john@example.com",
    "password": "password123"
  }'
```

**Response**: JWT token string (save this token)

#### Add Property (with JWT token)

```bash
curl -X POST http://localhost:8089/api/v1/properties/addProperty \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{
    "title": "Beautiful House",
    "description": "3 bedroom house",
    "price": 250000.0,
    "address": "123 Main St",
    "userId": "abc-123-uuid"
  }'
```

**Response** (201 Created):
```json
{
  "id": 1,
  "title": "Beautiful House",
  "description": "3 bedroom house",
  "price": 250000.0,
  "address": "123 Main St",
  "userId": "abc-123-uuid"
}
```

#### Get All Properties

```bash
curl -X GET http://localhost:8089/api/v1/properties/getProperties \
  -H "Authorization: Bearer <your_jwt_token>"
```

**Response**:
```json
[
  {
    "id": 1,
    "title": "Beautiful House",
    "description": "3 bedroom house",
    "price": 250000.0,
    "address": "123 Main St",
    "userId": "abc-123-uuid"
  }
]
```

#### Search Properties by Title

```bash
curl -X GET "http://localhost:8089/api/v1/properties/search?title=House" \
  -H "Authorization: Bearer <your_jwt_token>"
```

**Response**:
```json
{
  "properties": [
    {
      "id": 1,
      "title": "Beautiful House",
      "description": "3 bedroom house",
      "price": 250000.0,
      "address": "123 Main St",
      "userId": "abc-123-uuid"
    }
  ]
}
```

#### Search Properties by Price Range

```bash
curl -X GET "http://localhost:8089/api/v1/properties/search?minPrice=100000&maxPrice=300000" \
  -H "Authorization: Bearer <your_jwt_token>"
```

**Response**:
```json
{
  "properties": [
    {
      "id": 1,
      "title": "Beautiful House",
      "description": "3 bedroom house",
      "price": 250000.0,
      "address": "123 Main St",
      "userId": "abc-123-uuid"
    }
  ]
}
```

**Note**: Search endpoints return a `PropertiesDto` wrapper object with a `properties` array, not a direct array.

#### Get Properties by User ID

```bash
curl -X GET http://localhost:8089/api/v1/properties/getProperties/users/abc-123-uuid \
  -H "Authorization: Bearer <your_jwt_token>"
```

**Response**:
```json
[
  {
    "id": 1,
    "title": "Beautiful House",
    "description": "3 bedroom house",
    "price": 250000.0,
    "address": "123 Main St",
    "userId": "abc-123-uuid"
  }
]
```

#### Update Property (ADMIN only)

```bash
curl -X PUT http://localhost:8089/api/v1/properties/updateProperties/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_jwt_token>" \
  -d '{
    "title": "Updated House",
    "description": "4 bedroom house",
    "price": 275000.0,
    "address": "123 Main St",
    "userId": "abc-123-uuid"
  }'
```

#### Delete Property

```bash
curl -X DELETE http://localhost:8089/api/v1/properties/deleteProperty/1 \
  -H "Authorization: Bearer <your_jwt_token>"
```

**Response**: 204 No Content

## 🔧 Troubleshooting

### Common Issues and Solutions

#### Issue 1: "401 Unauthorized" Error

**Symptoms**: Getting 401 when accessing protected endpoints

**Possible Causes & Solutions**:

1. **Missing or Invalid JWT Token**
   - Ensure you're including the token in the Authorization header
   - Format: `Authorization: Bearer <your_token>` (include space after "Bearer")
   - Token might be expired (default is 60 seconds) - login again

2. **Token Format Incorrect**
   - Don't include "Bearer" prefix if using Postman's Bearer Token option
   - Include "Bearer" prefix if manually setting the header

3. **Token Expired**
   - Default token validity is 60 seconds (1 minute)
   - **Solution**: Login again to get a fresh token
   - **Long-term Fix**: Increase `jwt.validity` in `application.properties` (e.g., 86400000 for 24 hours)

#### Issue 2: "403 Forbidden" Error

**Symptoms**: Getting 403 even with a valid token

**Possible Causes & Solutions**:

1. **Insufficient Permissions**
   - User doesn't have the required role
   - Check what role is required for the endpoint
   - Register/login with a user that has the correct role

2. **Ownership Validation Failure**
   - PROPERTY_OWNER trying to access/modify someone else's property
   - **Solution**: Use your own user ID or ensure you're the property owner

3. **User Has No Roles**
   - User might not have any roles assigned
   - **Fix**: Check database - user should have at least `USER` role
   - Query: `SELECT * FROM user_roles WHERE user_id = 'your-user-id'`

#### Issue 3: "EXISTING_USER" Error on Registration

**Symptoms**: Cannot register with an email address

**Cause**: Email address already exists in the system

**Solutions**:
- Use a different email address
- Login with the existing user's credentials instead

#### Issue 4: Database Connection Issues

**Symptoms**: Application fails to start, database errors

**Solutions**:

1. **For MySQL**:
   - Verify MySQL is running: `mysql --version`
   - Check database exists: `SHOW DATABASES;`
   - Verify credentials in `application-dev.properties` or `application-prod.properties`
   - Check MySQL server is accessible: `mysql -u root -p`

2. **For H2**:
   - Check file permissions for `~/h2/propertydb`
   - Delete the database file if corrupted: `rm ~/h2/propertydb.mv.db`
   - Application will recreate it on next startup

#### Issue 5: Port 8089 Already in Use

**Symptoms**: Application fails to start with port binding error

**Solutions**:
- Find process using port: `netstat -ano | findstr :8089` (Windows) or `lsof -i :8089` (Linux/Mac)
- Kill the process or change the port in `application.properties`
- Set different port: `server.port=8090`

#### Issue 6: Property Not Found

**Symptoms**: Getting 404 when accessing a property

**Solutions**:
- Verify the property ID exists (property IDs are Long type, auto-increment)
- Check if property was deleted
- Ensure you're using the correct property ID format

#### Issue 7: Swagger UI Not Loading

**Symptoms**: Cannot access Swagger UI

**Solutions**:
- Use the correct URL: `http://localhost:8089/swagger-ui/index.html`
- Check application is running on port 8089
- Verify SpringDoc dependency is in pom.xml
- Clear browser cache

#### Issue 8: H2 Console Not Accessible

**Symptoms**: Cannot access H2 console

**Solutions**:
- H2 console is only available in `local` and `dev` profiles
- Check active profile: `spring.profiles.active=local`
- Use correct URL: `http://localhost:8089/h2-console`
- Ensure `spring.h2.console.enabled=true` in profile properties

### Debugging Tips

1. **Check Application Logs**
   - Logs are in `logs/my-app.log`
   - Enable SQL logging: `spring.jpa.show-sql=true` (already enabled in dev/local)

2. **Verify JWT Token**
   - Decode token at https://jwt.io (don't paste production tokens)
   - Check expiration time and user roles

3. **Test with Swagger UI**
   - Use the "Authorize" button to set your token
   - Test endpoints interactively

4. **Database Inspection**
   - Use H2 console (local/dev) to check data
   - Or use MySQL client for MySQL databases

## 🔒 Security Considerations

### Production Checklist

- [ ] **Change JWT Secret Key**: Generate a strong, unique secret key using `openssl rand -base64 64`
- [ ] **Increase Token Validity**: Set appropriate expiration time (e.g., 86400000 for 24 hours)
- [ ] **Use HTTPS**: Always use HTTPS in production
- [ ] **Database Security**: Use strong database credentials
- [ ] **Environment Variables**: Store sensitive data (passwords, secrets) in environment variables
- [ ] **Disable H2 Console**: Ensure `spring.h2.console.enabled=false` in production
- [ ] **Rate Limiting**: Implement rate limiting for login endpoints
- [ ] **Token Refresh**: Consider implementing refresh token mechanism
- [ ] **Logging**: Review and secure application logs (remove sensitive data)
- [ ] **CORS**: Configure CORS properly for your frontend domain
- [ ] **Input Validation**: Review validation rules for your use case
- [ ] **Secure Swagger UI**: Restrict access to Swagger UI in production (or disable it)

### Password Security

- Passwords are hashed using BCrypt (one-way hashing)
- Never store plain-text passwords
- Enforce strong password policies in production (minimum length, complexity requirements)
- Consider implementing password reset functionality

### JWT Token Security

- Tokens are signed with HMAC-SHA512
- Tokens include user roles for authorization
- Tokens expire after the configured validity period
- Store tokens securely on the client side (httpOnly cookies recommended)
- Consider implementing token blacklisting for logout functionality

### API Security Best Practices

- All endpoints (except registration and login) require authentication
- Role-based access control enforced at both controller and service layers
- Property ownership validated by email matching
- Input validation on all user inputs
- Error messages don't leak sensitive information

<!-- ## 🚧 Future Enhancements

- [ ] Refresh token mechanism for longer sessions
- [ ] Password reset functionality
- [ ] Email verification for user registration
- [ ] Role assignment endpoint (admin only)
- [ ] Token blacklisting for logout
- [ ] Rate limiting for API endpoints
- [ ] Property image upload
- [ ] Advanced search filters
- [ ] Pagination for property listings
- [ ] Audit logging for property changes
- [ ] Multi-tenancy support
- [ ] Property favorites/bookmarks
- [ ] Notification system -->

## 📝 API Endpoints Summary

### User Endpoints

- `POST /api/v1/users/registerUser` - Register a new user (Public)
  - Returns: UserDto with generated UUID
  
- `POST /api/v1/users/loginUser` - Login and get JWT token (Public)
  - Returns: JWT token string

### Property Endpoints

- `POST /api/v1/properties/addProperty` - Add a property (ADMIN, PROPERTY_OWNER)
  - PROPERTY_OWNER can only add properties for themselves
  - Returns: PropertyDto with generated ID

- `POST /api/v1/properties/addProperties` - Add multiple properties (ADMIN, PROPERTY_OWNER)
  - Returns: List of PropertyDto

- `GET /api/v1/properties/getProperties` - Get all properties (Authenticated)
  - Returns: List of PropertyDto
  
- `GET /api/v1/properties/getProperty/{id}` - Get property by ID (Authenticated)
  - Returns: PropertyDto or 404
  
- `GET /api/v1/properties/getProperties/users/{userId}` - Get properties by user (Authenticated)
  - PROPERTY_OWNER can only view their own properties
  - ADMIN can view any user's properties
  - Returns: List of PropertyDto

- `PUT /api/v1/properties/updateProperties/{id}` - Update property (ADMIN only)
  - Returns: Updated PropertyDto

- `DELETE /api/v1/properties/deleteProperty/{id}` - Delete property (ADMIN, PROPERTY_OWNER)
  - PROPERTY_OWNER can only delete their own properties
  - Returns: 204 No Content

- `GET /api/v1/properties/search?title={title}` - Search by title (Authenticated)
  - Returns: PropertiesDto (wrapper with properties array)

- `GET /api/v1/properties/search?minPrice={min}&maxPrice={max}` - Search by price range (Authenticated)
  - Returns: PropertiesDto (wrapper with properties array)

**Note**: All endpoints use `/api/v1/` prefix indicating API version 1.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## ✍️ Author

**Pooja Pandit**  
Master's Student in Information Science (Machine Learning)  
The University of Arizona

[![GitHub](https://img.shields.io/badge/GitHub-panditpooja-black?logo=github)](https://github.com/panditpooja)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-pooja--pandit-blue?logo=linkedin)](https://www.linkedin.com/in/pooja-pandit-177978135/)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- JWT library (jjwt) for JWT support
- SpringDoc OpenAPI for API documentation
- All contributors and open-source libraries used in this project
