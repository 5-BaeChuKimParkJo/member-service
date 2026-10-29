# Member Error Mapping Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans inline. Steps use checkbox syntax for tracking.

**Goal:** Return the defined member error responses without authentication advice intercepting them.
**Architecture:** Scope and prioritize the existing auth advice; retain the common advice as fallback and sanitize its runtime response.
**Tech Stack:** Java 17, Spring Boot 3.4/MVC, JUnit 5, MockMvc, H2; PostgreSQL in CI.
**Spec:** docs/superpowers/specs/2026-10-29-member-error-mapping.md

## Global Constraints

- Preserve successful/API route contracts, both error-code families and auth behavior.
- No new dependencies, business/schema changes, local Docker or production fault injection.
- Use the existing checkout/main and standing push/deployment authorization.

## Review Focus

- Either advice discovery order must preserve both member and auth domain errors.
- All three member route aliases must return 404/code 2002 for an absent member.
- Cache read/write failure must not change that missing-member response.
- Unexpected DB failures must remain 500 without exposing raw exception messages.
- Auth validation, malformed JSON, bad credentials and identity-verification errors must retain their contracts.

### Task 1: Correct and verify advice routing

Files: auth/common/exception/AuthBaseExceptionHandler.java,
common/exception/BaseExceptionHandler.java; tests:
common/MemberErrorResponseTest.java, member/MemberProfileCacheFallbackTest.java.
Interface: existing status + {code, message}; no public signature changes.

- [x] Add controller/advice regression tests; assert 404/2002, conflicts 409,
  generic DB failure 500, unchanged auth responses in both advice orders.
- [x] Strengthen real Spring/cache/H2 missing-member assertions to exact 404/2002.
- [x] Run targeted tests and observe the expected 500-versus-404 failure.
- [x] Apply scoped auth advice/priority and generic common runtime response.
- [x] Run all local tests except the existing Docker-only transaction test,
  Spotless, ArchUnit, bootJar and commit-convention checks.
- [ ] Obtain one independent diff review; address material findings and commit.

### Task 2: Publish and verify

- [ ] Push main; verify the exact-SHA full CI and GHCR publication.
- [ ] Confirm exact new image/rollout, GitOps tag and Argo health.
- [ ] Read-only live check: missing profile 404/code 2002; existing seller 200.
- [ ] Record evidence/limits, update stale cache documentation and sync GitOps.
