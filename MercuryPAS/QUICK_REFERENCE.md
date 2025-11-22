# Mercury PAS - Quick Reference Guide

## Authentication

### Register
```bash
POST /api/auth/register
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "CUSTOMER",
  "dob": "1990-01-01",
  "licenseNumber": "DL123456"
}
```

### Login
```bash
POST /api/auth/login
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:** Returns JWT token - use in `Authorization: Bearer <token>` header

---

## Policy Operations

### Create Policy (with Term & State Code)
```bash
POST /api/policies/create
Authorization: Bearer <token>
{
  "quoteId": 1,
  "agentId": 2,
  "startDate": "2024-01-01",
  "endDate": "2024-07-01",
  "term": "SIX_MONTHS",        # or "TWELVE_MONTHS"
  "stateCode": "CA"            # 2-letter state code
}
```

**Policy Number Format:** `{STATE}-{TIMESTAMP}` (15 characters)
- Example: `CA-123456789012`

### Generate Policy PDF
```bash
GET /api/policies/{id}/pdf
Authorization: Bearer <token>
```
Returns PDF file download

---

## Claim Operations

### File Claim (with State Code)
```bash
POST /api/claims/file
Authorization: Bearer <token>
{
  "policyId": 1,
  "customerId": 1,
  "description": "Accident description",
  "stateCode": "CA"            # 2-letter state code
}
```

**Claim Number Format:** `{STATE}-CLM-{8_CHAR_UUID}`
- Example: `CA-CLM-ABC12345`

---

## Policy Term Values

- `SIX_MONTHS` - 6-month policy (value: "6MON")
- `TWELVE_MONTHS` - 12-month policy (value: "12MON")

---

## State Codes

Use standard 2-letter US state codes:
- CA (California)
- NY (New York)
- TX (Texas)
- FL (Florida)
- etc.

---

## Motor Vehicle Report (MVR)

**Entity:** `MotorVehicleReport`
**Table:** `motor_vehicle_reports`

**Fields:**
- `customer` - User reference
- `licenseNumber` - Driver's license
- `stateCode` - State code
- `violations` - Number of violations
- `accidents` - Number of accidents
- `points` - License points
- `reportDate` - Report date
- `drivingHistory` - History details

**Usage:** Check MVR before accepting quotes:
```java
if (mvr.getPoints() >= 3 || mvr.getViolations() >= 3) {
    // Reject quote
}
```

---

## All Endpoints Summary

| Endpoint | Method | Auth | Description |
|----------|--------|------|-------------|
| `/api/auth/register` | POST | No | Register user |
| `/api/auth/login` | POST | No | Login user |
| `/api/policies/create` | POST | Yes | Create policy |
| `/api/policies/{id}` | GET | Yes | Get policy |
| `/api/policies/{id}/pdf` | GET | Yes | **Generate PDF** |
| `/api/claims/file` | POST | Yes | File claim |
| `/api/quotes/generate` | POST | Yes | Generate quote |

---

## cURL Examples

### Login and Get Token
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}' \
  | jq -r '.token')
```

### Create Policy
```bash
curl -X POST http://localhost:8080/api/policies/create \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "quoteId": 1,
    "agentId": 2,
    "startDate": "2024-01-01",
    "endDate": "2024-07-01",
    "term": "SIX_MONTHS",
    "stateCode": "CA"
  }'
```

### Download Policy PDF
```bash
curl -X GET http://localhost:8080/api/policies/1/pdf \
  -H "Authorization: Bearer $TOKEN" \
  --output policy_1.pdf
```

---

## Key Changes Summary

1. **Security:** JWT + OAuth2 authentication required for all APIs
2. **Policy Term:** Added 6MON/12MON term field
3. **Claim Numbers:** Format `{STATE}-CLM-{UUID}`
4. **Policy Numbers:** Format `{STATE}-{TIMESTAMP}` (15 chars)
5. **PDF Generation:** `GET /api/policies/{id}/pdf`
6. **MVR Table:** Stores driving history for quote decisions

