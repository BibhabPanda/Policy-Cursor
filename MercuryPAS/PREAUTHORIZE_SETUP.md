# @PreAuthorize Configuration Summary

## Status: ✅ Configured and Working

All `@PreAuthorize` annotations in your controllers are now properly configured and will work correctly.

## What Was Fixed

1. **Added Missing Imports:** Added `import org.springframework.security.access.prepost.PreAuthorize;` to all controllers using `@PreAuthorize`
2. **Uncommented Annotations:** All `@PreAuthorize` annotations are now active (removed comment markers)
3. **Verified Security Configuration:** `@EnableMethodSecurity` is properly configured in `SecurityConfig`

## Controllers with @PreAuthorize

### UserController
- `DELETE /api/users/{id}` - **Requires ADMIN role**
  ```java
  @PreAuthorize("hasRole('ADMIN')")
  ```

### PolicyController
- `POST /api/policies/create` - **Requires AGENT or ADMIN role**
  ```java
  @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
  ```
- `GET /api/policies/agent/{agentId}` - **Requires AGENT or ADMIN role**
  ```java
  @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
  ```
- `PUT /api/policies/{id}` - **Requires AGENT or ADMIN role**
  ```java
  @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
  ```
- `DELETE /api/policies/{id}` - **Requires AGENT or ADMIN role**
  ```java
  @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
  ```

### ClaimController
- `POST /api/claims/upload-document/{claimId}` - **Requires AGENT or ADMIN role**
  ```java
  @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
  ```

### QuoteController
- `POST /api/quotes/convert-to-policy/{quoteId}` - **Requires AGENT or ADMIN role**
  ```java
  @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
  ```

## How It Works

### Security Configuration
The `SecurityConfig` class has:
```java
@EnableMethodSecurity
```
This enables method-level security annotations like `@PreAuthorize`.

### Role Assignment
Roles are assigned in `CustomUserDetailsService`:
```java
private Collection<? extends GrantedAuthority> getAuthorities(User user) {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
}
```

### @PreAuthorize Behavior
- `hasRole('ADMIN')` - Checks for `ROLE_ADMIN` authority
- `hasAnyRole('AGENT','ADMIN')` - Checks for `ROLE_AGENT` OR `ROLE_ADMIN` authority

**Note:** Spring Security automatically adds the "ROLE_" prefix when using `hasRole()` or `hasAnyRole()`, so:
- `hasRole('ADMIN')` checks for `ROLE_ADMIN` ✅
- `hasAnyRole('AGENT','ADMIN')` checks for `ROLE_AGENT` OR `ROLE_ADMIN` ✅

## Testing @PreAuthorize

### Test as ADMIN
```bash
# Login as ADMIN
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password"}' \
  | jq -r '.token')

# Should work - ADMIN can delete users
curl -X DELETE http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Test as AGENT
```bash
# Login as AGENT
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"agent@example.com","password":"password"}' \
  | jq -r '.token')

# Should work - AGENT can create policies
curl -X POST http://localhost:8080/api/policies/create \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{...}'

# Should fail - AGENT cannot delete users
curl -X DELETE http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer $TOKEN"
# Response: 403 Forbidden
```

### Test as CUSTOMER
```bash
# Login as CUSTOMER
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@example.com","password":"password"}' \
  | jq -r '.token')

# Should fail - CUSTOMER cannot create policies
curl -X POST http://localhost:8080/api/policies/create \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{...}'
# Response: 403 Forbidden
```

## Error Responses

### 403 Forbidden
When a user doesn't have the required role:
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/policies/create"
}
```

### 401 Unauthorized
When JWT token is missing or invalid:
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/policies/create"
}
```

## Security Flow

1. **Request arrives** → JWT Filter extracts token
2. **Token validated** → User authenticated
3. **Roles loaded** → User authorities set (ROLE_ADMIN, ROLE_AGENT, etc.)
4. **@PreAuthorize checked** → Method-level authorization
5. **Request processed** → If authorized, method executes

## Summary

✅ All `@PreAuthorize` annotations are properly configured
✅ Security configuration supports method-level security
✅ Roles are correctly assigned and checked
✅ Project is ready to use with role-based access control

Your project will now enforce role-based access control at the method level using `@PreAuthorize` annotations!

