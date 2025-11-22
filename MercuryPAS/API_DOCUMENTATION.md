# Mercury Policy Administration System - API Documentation

## Table of Contents
1. [Security Implementation (JWT & OAuth2)](#security-implementation)
2. [Policy Term Feature](#policy-term-feature)
3. [State Code-Based Number Generation](#state-code-based-number-generation)
4. [PDF Generation](#pdf-generation)
5. [Motor Vehicle Report (MVR)](#motor-vehicle-report)
6. [Complete API Endpoints](#complete-api-endpoints)

---

## Security Implementation

### Overview
The system now uses JWT (JSON Web Tokens) and OAuth2 for authentication and authorization. All REST APIs are secured except the authentication endpoints.

### Authentication Endpoints

#### 1. Register User
**Endpoint:** `POST /api/auth/register`

**Description:** Register a new user in the system.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123",
  "role": "CUSTOMER",
  "dob": "1990-01-15",
  "licenseNumber": "DL123456"
}
```

**Role Options:**
- `ADMIN` - System administrator
- `AGENT` - Insurance agent
- `CUSTOMER` - Policy customer

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "role": "CUSTOMER"
  }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "SecurePassword123",
    "role": "CUSTOMER",
    "dob": "1990-01-15",
    "licenseNumber": "DL123456"
  }'
```

#### 2. Login
**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate user and receive JWT token.

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "role": "CUSTOMER"
  }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePassword123"
  }'
```

### Using JWT Tokens

After receiving a JWT token from login/register, include it in the Authorization header for all protected endpoints:

```
Authorization: Bearer <your-jwt-token>
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/policies/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Security Configuration

- **JWT Secret:** Configured in `application.properties` as `jwt.secret`
- **Token Expiration:** 24 hours (86400000 milliseconds)
- **Password Encryption:** BCrypt
- **Role-Based Access:** All API endpoints require appropriate roles (ADMIN, AGENT, or CUSTOMER)

---

## Policy Term Feature

### Overview
Policies now support two term options: 6 months or 12 months. This is specified when creating a policy.

### Policy Term Enum

**Values:**
- `SIX_MONTHS` - 6-month policy term (value: "6MON")
- `TWELVE_MONTHS` - 12-month policy term (value: "12MON")

### Usage in Create Policy

When creating a policy, you must specify the term:

**Request Body:**
```json
{
  "quoteId": 1,
  "agentId": 2,
  "startDate": "2024-01-01",
  "endDate": "2024-07-01",
  "term": "SIX_MONTHS",
  "stateCode": "CA"
}
```

**Response includes term:**
```json
{
  "id": 1,
  "policyNumber": "CA-123456789012",
  "term": "SIX_MONTHS",
  "status": "ACTIVE",
  "premiumAmount": 1000.00,
  ...
}
```

---

## State Code-Based Number Generation

### Claim Number Generation

Claim numbers are now generated based on the state code provided when filing a claim.

**Format:** `{STATE_CODE}-CLM-{8_CHAR_UUID}`

**Example:**
- California: `CA-CLM-ABC12345`
- New York: `NY-CLM-XYZ98765`
- Texas: `TX-CLM-DEF45678`

**Usage:**
```json
POST /api/claims/file
{
  "policyId": 1,
  "customerId": 1,
  "description": "Vehicle accident on highway",
  "stateCode": "CA"
}
```

**Response:**
```json
{
  "id": 1,
  "claimNumber": "CA-CLM-ABC12345",
  "status": "NEW",
  "description": "Vehicle accident on highway",
  ...
}
```

### Policy Number Generation

Policy numbers are generated based on state code and are exactly 15 characters long.

**Format:** `{STATE_CODE}-{TIMESTAMP}` (padded/truncated to 15 characters)

**Examples:**
- California: `CA-123456789012` (15 chars)
- New York: `NY-987654321098` (15 chars)
- Texas: `TX-456789012345` (15 chars)

**Usage:**
```json
POST /api/policies/create
{
  "quoteId": 1,
  "agentId": 2,
  "startDate": "2024-01-01",
  "endDate": "2024-07-01",
  "term": "SIX_MONTHS",
  "stateCode": "CA"
}
```

**Response:**
```json
{
  "id": 1,
  "policyNumber": "CA-123456789012",
  "term": "SIX_MONTHS",
  ...
}
```

**State Code Requirements:**
- Must be a valid 2-letter US state code (e.g., CA, NY, TX, FL)
- Case-insensitive (will be converted to uppercase)
- Required for both claim and policy creation

---

## PDF Generation

### Overview
Users can generate PDF documents containing complete policy details after a quote is converted to a policy.

### Generate Policy PDF

**Endpoint:** `GET /api/policies/{id}/pdf`

**Description:** Generates and downloads a PDF document with policy details.

**Authentication:** Required (JWT token)

**Response:**
- Content-Type: `application/pdf`
- File download with name: `policy_{id}.pdf`

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/policies/1/pdf \
  -H "Authorization: Bearer <your-jwt-token>" \
  --output policy_1.pdf
```

**JavaScript Example:**
```javascript
fetch('/api/policies/1/pdf', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
.then(response => response.blob())
.then(blob => {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'policy_1.pdf';
  a.click();
});
```

### PDF Contents

The generated PDF includes:

1. **Policy Details:**
   - Policy Number
   - Status
   - Term (6MON or 12MON)
   - Premium Amount
   - Start Date
   - End Date

2. **Customer Information:**
   - Full Name
   - Email Address

3. **Vehicle Information:**
   - Make
   - Model
   - Year
   - VIN

4. **Agent Information:**
   - Agent Name
   - Agent Email

**Note:** PDF generation is available for all authenticated users (ADMIN, AGENT, CUSTOMER).

---

## Motor Vehicle Report (MVR)

### Overview
The Motor Vehicle Report (MVR) table stores customer driving history from their state, which helps in accepting or rejecting quotes.

### Database Schema

**Table:** `motor_vehicle_reports`

**Fields:**
- `id` - Primary key
- `customer_id` - Foreign key to users table
- `license_number` - Driver's license number
- `state_code` - 2-letter state code
- `violations` - Number of traffic violations
- `accidents` - Number of accidents
- `points` - Driver's license points
- `report_date` - Date of the MVR report
- `driving_history` - Detailed driving history (text)

**Indexes:**
- `idx_license_number` - On license_number
- `idx_state` - On state_code

### Repository Methods

**MotorVehicleReportRepository** provides:
- `findByCustomerAndLicenseNumber(User customer, String licenseNumber)` - Find MVR by customer and license
- `findByCustomer(User customer)` - Find MVR by customer

### Usage Example

```java
// In your service class
@Autowired
private MotorVehicleReportRepository mvrRepository;

// Find MVR for a customer
Optional<MotorVehicleReport> mvr = mvrRepository.findByCustomer(customer);

if (mvr.isPresent()) {
    MotorVehicleReport report = mvr.get();
    // Use violations, accidents, points to determine quote eligibility
    if (report.getPoints() >= 3) {
        // Reject quote
    }
}
```

### Integration with Quote Generation

You can integrate MVR checking in your quote generation logic:

```java
// Example: Check MVR before accepting quote
MotorVehicleReport mvr = mvrRepository.findByCustomer(customer)
    .orElse(null);

if (mvr != null) {
    // Reject quote if violations >= 3
    if (mvr.getViolations() >= 3) {
        throw new RuntimeException("Quote rejected: Too many violations");
    }
    
    // Reject quote if points >= 6
    if (mvr.getPoints() >= 6) {
        throw new RuntimeException("Quote rejected: Too many license points");
    }
}
```

---

## Complete API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login user | No |

### User Endpoints

| Method | Endpoint | Description | Auth Required | Roles |
|--------|----------|-------------|---------------|-------|
| GET | `/api/users/me` | Get current user | Yes | All |
| PUT | `/api/users/update-profile` | Update user profile | Yes | All |
| GET | `/api/users/all` | Get all users | Yes | All |
| DELETE | `/api/users/{id}` | Delete user | Yes | All |

### Policy Endpoints

| Method | Endpoint | Description | Auth Required | Roles |
|--------|----------|-------------|---------------|-------|
| POST | `/api/policies/create` | Create policy | Yes | ADMIN, AGENT, CUSTOMER |
| GET | `/api/policies/{id}` | Get policy by ID | Yes | ADMIN, AGENT, CUSTOMER |
| GET | `/api/policies/customer/{customerId}` | Get policies by customer | Yes | ADMIN, AGENT, CUSTOMER |
| GET | `/api/policies/agent/{agentId}` | Get policies by agent | Yes | ADMIN, AGENT, CUSTOMER |
| PUT | `/api/policies/{id}` | Update policy | Yes | ADMIN, AGENT, CUSTOMER |
| DELETE | `/api/policies/{id}` | Delete policy | Yes | ADMIN, AGENT, CUSTOMER |
| **GET** | **`/api/policies/{id}/pdf`** | **Generate policy PDF** | **Yes** | **ADMIN, AGENT, CUSTOMER** |

### Quote Endpoints

| Method | Endpoint | Description | Auth Required | Roles |
|--------|----------|-------------|---------------|-------|
| POST | `/api/quotes/generate` | Generate quote | Yes | ADMIN, AGENT, CUSTOMER |
| POST | `/api/quotes/save` | Save quote | Yes | ADMIN, AGENT, CUSTOMER |
| GET | `/api/quotes/{id}` | Get quote by ID | Yes | ADMIN, AGENT, CUSTOMER |
| GET | `/api/quotes/customer/{customerId}` | Get quotes by customer | Yes | ADMIN, AGENT, CUSTOMER |
| POST | `/api/quotes/convert-to-policy/{quoteId}` | Convert quote to policy | Yes | ADMIN, AGENT, CUSTOMER |

### Claim Endpoints

| Method | Endpoint | Description | Auth Required | Roles |
|--------|----------|-------------|---------------|-------|
| POST | `/api/claims/file` | File a claim | Yes | ADMIN, AGENT, CUSTOMER |
| GET | `/api/claims/{id}` | Get claim by ID | Yes | ADMIN, AGENT, CUSTOMER |
| GET | `/api/claims/policy/{policyId}` | Get claims by policy | Yes | ADMIN, AGENT, CUSTOMER |
| POST | `/api/claims/upload-document/{claimId}` | Upload claim document | Yes | ADMIN, AGENT, CUSTOMER |

---

## Request/Response Examples

### Create Policy (with Term and State Code)

**Request:**
```bash
POST /api/policies/create
Authorization: Bearer <token>
Content-Type: application/json

{
  "quoteId": 1,
  "agentId": 2,
  "startDate": "2024-01-01",
  "endDate": "2024-07-01",
  "term": "SIX_MONTHS",
  "stateCode": "CA"
}
```

**Response:**
```json
{
  "id": 1,
  "policyNumber": "CA-123456789012",
  "quoteId": 1,
  "vehicleId": 1,
  "customerId": 1,
  "agentId": 2,
  "startDate": "2024-01-01",
  "endDate": "2024-07-01",
  "premiumAmount": 1000.00,
  "status": "ACTIVE",
  "term": "SIX_MONTHS"
}
```

### File Claim (with State Code)

**Request:**
```bash
POST /api/claims/file
Authorization: Bearer <token>
Content-Type: application/json

{
  "policyId": 1,
  "customerId": 1,
  "description": "Rear-end collision on Highway 101",
  "stateCode": "CA"
}
```

**Response:**
```json
{
  "id": 1,
  "claimNumber": "CA-CLM-ABC12345",
  "policyId": 1,
  "customerId": 1,
  "description": "Rear-end collision on Highway 101",
  "status": "NEW",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### Generate Policy PDF

**Request:**
```bash
GET /api/policies/1/pdf
Authorization: Bearer <token>
```

**Response:**
- Content-Type: `application/pdf`
- Binary PDF file download

---

## Configuration

### JWT Configuration

In `application.properties`:
```properties
# JWT Configuration
jwt.secret=MercuryPolicyAdministrationSystemSecretKeyForJWTTokenGeneration2024
jwt.expiration=86400000
```

**Note:** Change the `jwt.secret` in production to a secure, randomly generated key.

### Dependencies Added

The following dependencies were added to `pom.xml`:

1. **Spring Security:**
   - `spring-boot-starter-security`
   - `spring-boot-starter-oauth2-resource-server`

2. **JWT:**
   - `jjwt-api` (v0.12.3)
   - `jjwt-impl` (v0.12.3)
   - `jjwt-jackson` (v0.12.3)

3. **PDF Generation:**
   - `itextpdf` (v5.5.13.3)

---

## Error Handling

### Authentication Errors

**401 Unauthorized:**
```json
{
  "message": "Invalid email or password",
  "status": "UNAUTHORIZED"
}
```

**403 Forbidden:**
```json
{
  "message": "Access denied",
  "status": "FORBIDDEN"
}
```

### Validation Errors

**400 Bad Request:**
```json
{
  "message": "Validation failed",
  "errors": "{fieldName=error message}",
  "status": "VALIDATION_ERROR"
}
```

### Not Found Errors

**404 Not Found:**
```json
{
  "message": "Policy not found",
  "status": "NOT_FOUND"
}
```

---

## Testing the API

### Using Postman

1. **Register/Login:**
   - Create a POST request to `/api/auth/register` or `/api/auth/login`
   - Copy the `token` from the response

2. **Set Authorization:**
   - Go to Authorization tab
   - Select "Bearer Token"
   - Paste the token

3. **Test Protected Endpoints:**
   - All other endpoints will use the token automatically

### Using cURL

```bash
# 1. Login
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}' \
  | jq -r '.token')

# 2. Use token for protected endpoints
curl -X GET http://localhost:8080/api/policies/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## Summary of Changes

### New Features
1. ✅ JWT and OAuth2 security implementation
2. ✅ Policy Term field (6 months or 12 months)
3. ✅ State code-based claim number generation
4. ✅ State code-based policy number generation (15 characters)
5. ✅ PDF generation for policy details
6. ✅ Motor Vehicle Report (MVR) entity and repository

### Modified Files
- `Policy.java` - Added `term` field
- `CreatePolicyRequest.java` - Added `term` and `stateCode` fields
- `FileClaimRequest.java` - Added `stateCode` field
- `PolicyResponse.java` - Added `term` field
- `ClaimServiceImpl.java` - Updated claim number generation
- `PolicyServiceImpl.java` - Updated policy number generation
- `PolicyController.java` - Added PDF endpoint
- `pom.xml` - Added security and PDF dependencies

### New Files
- `PolicyTerm.java` - Enum for policy terms
- `MotorVehicleReport.java` - MVR entity
- `MotorVehicleReportRepository.java` - MVR repository
- `PolicyPdfService.java` - PDF service interface
- `PolicyPdfServiceImpl.java` - PDF service implementation
- Security classes (JWT, OAuth2, etc.)

---

## Support

For issues or questions, please refer to the project documentation or contact the development team.

