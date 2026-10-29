# Member API error mapping

The user approved correcting missing-member HTTP 500 responses to 404, retaining
real infrastructure failures as 500 and preserving authentication responses.
Implement inline in the existing checkout, then push and verify deployment.

## Approach

Both existing controller advices are global. The authentication advice can catch
a member BaseException with its RuntimeException fallback before the member advice.
Scope the authentication advice to the authentication web adapter package and give
it explicit priority over the common fallback. Keep the common advice as the global
fallback. This preserves the two existing error-code families and JSON fields
without merging domain exceptions or adding a framework/dependency.

Changing only advice order would break authentication in the opposite direction;
merging all exception models would unnecessarily change application contracts.

## Expected behavior and constraints

- Member NO_EXISTS_MEMBER: HTTP 404, code 2002, existing public message.
- Member nickname conflicts: HTTP 409, existing codes 2001/2004.
- Member unexpected runtime/DB errors: HTTP 500, code 500, generic public message;
  never return the raw exception message to clients.
- Authentication/identity-verification error status, code and message remain as-is.
- Preserve canonical and legacy routes, successful response bodies and cache fallback.
- No business rules, credentials, schema, dependencies or filter authentication changes.
- No local Docker; existing PostgreSQL transaction test runs in unfiltered CI.
- Production checks are read-only; no forced Redis/DB outage or real transactions.

Use MVC tests with the actual controllers, mappers and advice (both registration
orders) for error mapping, plus the existing real Spring/JPA/cache test for 404
with a healthy or failing cache. Inject errors only at the application-port boundary.
