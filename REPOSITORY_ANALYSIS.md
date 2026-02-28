# FundStack Backend Repository Analysis

## 1) Repository Overview

This repository is a Spring Boot (Java 21) backend service focused on mutual fund master data ingestion and CRUD operations for fund houses, schemes, AMFI scheme details, and scheme plan options.

Key platform choices:
- Spring Boot 4.0.2
- Spring Web MVC
- Spring Data JPA
- Spring Security
- PostgreSQL driver:
- **Controller layer** for REST endpoint
- Lombok

## 2) High-Level Architecture

The codebase follows a layered structures.
- **Service layer** for business workflows and AMFI orchestration.
- **Repository layer** built on Spring Data.
- **Entity/model layer** for persistence and AMFI API DTO mapping.

A generic abstraction is used for CRUD operations:
- `CrudController<E, ID>` exposes common endpoints (`POST`, `GET`, `DELETE`).
- `CrudService<E, ID>` centralizes persistence operations (`save`, `findAll`, etc.).
- `CrudRepositoryBase<E, ID>` extends `JpaRepository`.

## 3) Main Functional Flows

### 3.1 CRUD Endpoints
- `/fund-house` via `FundHouseController`
- `/scheme` via `SchemeController`
- `/scheme-plan` via `SchemePlanOptionController`

### 3.2 AMFI Scheme Ingestion
`SchemeService#getSchemesList(amfiId)`:
1. Loads fund house by AMFI id.
2. Fetches scheme list from AMFI API.
3. Filters out existing schemes already stored for that fund house.
4. Saves only new schemes.
5. Triggers follow-up detail processing per saved scheme.

### 3.3 Scheme Detail Enrichment
`SchemeDetailsService#saveSchemeDetails(...)`:
1. Fetches detailed AMFI scheme metadata.
2. Fetches NAV payload for the scheme.
3. Persists `AmfiSchemeDetails` associated with `Scheme`.
4. Current plan-option persistence logic is incomplete (placeholder save).

## 4) Data Model Notes

### Core entities
- `FundHouse` (one-to-many `Scheme`)
- `Scheme` (many-to-one `FundHouse`, one-to-one `AmfiSchemeDetails`, one-to-many `SchemePlanOption`)
- `AmfiSchemeDetails` (mapped to a single `Scheme`)
- `SchemePlanOption` (many-to-one `Scheme`)

### Auditing
`BaseEntity` includes:
- `createdAt`, `updatedAt`
- `createdBy`, `updatedBy`

`AuditListener` populates these fields using security context username or `system` fallback.

## 5) Configuration & Environment

- Default profile: `local`
- Local DB configured for PostgreSQL on `localhost:5432/fundStack`
- Hibernate DDL mode: `update`
- AMFI endpoint URLs are externalized in `application-local.yml`

## 6) Security Posture

Current `SecurityFilterChain` permits all requests (`/**`), effectively disabling endpoint protection while maintaining stateless session policy.

Implication: suitable for early-stage development/testing, but not production-safe without tightening request authorization.

## 7) Findings / Risks

1. **Build-time risk: Jackson import namespace mismatch**
   - `AmfiService` imports `tools.jackson.*` types instead of `com.fasterxml.jackson.*` for `ObjectMapper` and `TypeReference`.
   - This can fail compilation unless non-standard `tools.jackson` classes are present.

2. **Asynchronous/event design inconsistency**
   - `SchemeOrchestrationService#handleSchemeCreated` is annotated with `@TransactionalEventListener`, but it is invoked directly as a method call from `SchemeService`.
   - `ApplicationEventPublisher` is injected in `SchemeService` but not used.
   - This suggests mixed patterns (direct invocation vs event publication) and should be unified.

3. **Potential null association issue in detail endpoint**
   - `SchemeController#getSchemeDetails` passes `null` as `Scheme` into `saveSchemeDetails`.
   - If DB schema requires non-null relation, this can fail at persistence time.

4. **Incomplete NAV/plan option mapping**
   - `SchemeDetailsService` persists an empty `SchemePlanOption` builder object when NAV data exists.
   - Mandatory DB fields on `SchemePlanOption` may cause runtime errors.

5. **Schema management caution**
   - `ddl-auto: update` is convenient but risky for controlled production migrations.

6. **Unused/dead code indicators**
   - `SchemeCreatedEvent` exists but is not used.
   - Async executor bean in `AsyncConfig` is commented out.

## 8) Suggested Next Steps

### Short-term (stabilization)
1. Fix `AmfiService` imports to standard Jackson packages.
2. Decide on one orchestration strategy:
   - Either publish domain events + `@TransactionalEventListener`
   - Or keep direct async method invocation and remove event-only annotations/injections.
3. Correct `/scheme/detail/{mfId}/{schemeId}` behavior to avoid null `Scheme` relation or split into a read-only AMFI fetch endpoint.
4. Implement full NAV-to-`SchemePlanOption` mapping with required field population.
5. Add integration tests for:
   - Scheme ingestion deduplication
   - Detail enrichment happy path
   - Error handling when AMFI returns empty payload.

### Medium-term
1. Introduce migration tooling (Flyway/Liquibase).
2. Tighten security authorization rules for non-public endpoints.
3. Add API documentation (OpenAPI/Swagger).
4. Add resilience patterns for AMFI integrations (timeouts/retry/circuit breaker).

## 9) Operational Readiness Snapshot

Current maturity appears **prototype to early development**:
- Strong foundational structure for CRUD + external integration.
- Several key correctness and hardening gaps before production deployment.
